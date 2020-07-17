package com.bugevil.mhelmi.weatherforecast.features.home.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bugevil.mhelmi.weatherforecast.R
import com.bugevil.mhelmi.weatherforecast.databinding.ActivityMainBinding
import com.bugevil.mhelmi.weatherforecast.features.home.ui.fragments.CityWeatherFragmentArgs
import com.bugevil.mhelmi.weatherforecast.features.home.ui.viewmodels.WeatherViewModel
import com.bugevil.mhelmi.weatherforecast.utils.Const
import com.bugevil.mhelmi.weatherforecast.utils.SessionManager
import com.bugevil.mhelmi.weatherforecast.utils.appComponent
import com.bugevil.mhelmi.weatherforecast.utils.getCityAndCountry
import com.bugevil.mhelmi.weatherforecast.utils.getLastKnownLocation
import com.bugevil.mhelmi.weatherforecast.utils.loge
import com.bugevil.mhelmi.weatherforecast.utils.result.Resource
import com.bugevil.mhelmi.weatherforecast.utils.result.ResourceType
import com.bugevil.mhelmi.weatherforecast.utils.showErrorMessage
import com.bugevil.mhelmi.weatherforecast.utils.showLoading
import com.bugevil.mhelmi.weatherforecast.utils.viewmodel.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  @Inject lateinit var sessionManager: SessionManager
  @Inject lateinit var viewModelFactory: ViewModelFactory
  private val weatherViewModel: WeatherViewModel by viewModels { viewModelFactory }
  private lateinit var navController: NavController
  private val requestLocationPermissionLauncher by lazy {
    registerForActivityResult(RequestMultiplePermissions()) { grantedPermissions ->
      var isGranted = true
      grantedPermissions.forEach { if (!it.value) isGranted = false }
      if (isGranted) {
        getCurrentLocation()
      } else {
        setDefaultCityLondonUK()
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbar)
    appComponent.inject(this)
    setupNavigationComponent()
    populateCitiesFistTime()
  }

  private fun setupNavigationComponent() {
    navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    NavigationUI.setupActionBarWithNavController(this, navController)
    navController.addOnDestinationChangedListener { _, destination, arguments ->
      loge("OnDestinationChangedListener called")
      title = when (destination.id) {
        R.id.cityWeatherFragment -> arguments?.let { CityWeatherFragmentArgs.fromBundle(it).city.name }
        R.id.searchCityFragment -> getString(R.string.search_city_fragment_label)
        else -> getString(R.string.app_name)
      }
    }
  }

  private fun populateCitiesFistTime() {
    weatherViewModel.populateCitiesFistTime()
      .observe(this, this::handlePopulateFirstTimeResult)
  }

  private fun handlePopulateFirstTimeResult(result: Resource<Unit>) {
    when (result.resourceType) {
      ResourceType.LOADING -> setPopulateCitiesLoading(true, getString(R.string.loading_first_time))
      ResourceType.SUCCESS -> {
        setPopulateCitiesLoading(false)
        checkLocationPermission()
      }
      else -> showPopulateCitiesError(getString(R.string.error_failed_setup_app))
    }
  }

  private fun setPopulateCitiesLoading(isLoading: Boolean, message: String? = null) =
    binding.populateCitiesLayoutProgress.showLoading(isLoading, message)

  private fun showPopulateCitiesError(msg: String) =
    binding.populateCitiesLayoutProgress.showErrorMessage(msg)

  private fun checkLocationPermission() {
    if (sessionManager.canRequestLocation) {
      if (VERSION.SDK_INT >= VERSION_CODES.N) {
        if (
          ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
          ) == PackageManager.PERMISSION_GRANTED
          &&
          ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
          ) == PackageManager.PERMISSION_GRANTED
        ) {
          getCurrentLocation()
        } else {
          requestLocationPermissionLauncher.launch(
            arrayOf(
              Manifest.permission.ACCESS_COARSE_LOCATION,
              Manifest.permission.ACCESS_FINE_LOCATION
            )
          )
        }
      } else {
        getCurrentLocation()
      }
    }
  }

  @SuppressLint("MissingPermission")
  private fun getCurrentLocation() {
    val location = getLastKnownLocation()
    loge("getCurrentLocation: location =  ${location?.toString() ?: "null"}")
    if (location != null) {
      val pair = location.getCityAndCountry(this)
      setDefaultCity(pair.first, pair.second)
    } else {
      setDefaultCityLondonUK()
    }
  }

  private fun setDefaultCityLondonUK() {
    setDefaultCity(Const.LONDON, Const.UK)
  }

  private fun setDefaultCity(cityName: String?, countryCode: String?) {
    weatherViewModel.setDefaultCity(cityName, countryCode)
      .observe(this@MainActivity, this@MainActivity::handleDefaultCityResult)
  }

  private fun handleDefaultCityResult(result: Resource<Unit>) {
    sessionManager.canRequestLocation = false
    when (result.resourceType) {
      ResourceType.LOADING -> setLoading(true)
      ResourceType.SUCCESS -> setLoading(false)
      else -> showError(getString(R.string.error_no_favorite_cities_found))
    }
  }

  private fun setLoading(isLoading: Boolean) = binding.layoutProgress.showLoading(isLoading)

  private fun showError(msg: String) = binding.layoutProgress.showErrorMessage(msg)

  override fun onSupportNavigateUp(): Boolean {
    return NavigationUI.navigateUp(navController, null)
  }
}