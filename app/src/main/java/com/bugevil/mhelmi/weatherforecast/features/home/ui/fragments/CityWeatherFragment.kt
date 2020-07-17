package com.bugevil.mhelmi.weatherforecast.features.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugevil.mhelmi.weatherforecast.R
import com.bugevil.mhelmi.weatherforecast.databinding.FragmentCityWeatherBinding
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.City
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.WeatherData
import com.bugevil.mhelmi.weatherforecast.features.home.ui.adapters.WeatherAdapter
import com.bugevil.mhelmi.weatherforecast.features.home.ui.viewmodels.WeatherViewModel
import com.bugevil.mhelmi.weatherforecast.utils.appComponent
import com.bugevil.mhelmi.weatherforecast.utils.loge
import com.bugevil.mhelmi.weatherforecast.utils.result.Resource
import com.bugevil.mhelmi.weatherforecast.utils.result.ResourceType.LOADING
import com.bugevil.mhelmi.weatherforecast.utils.result.ResourceType.SUCCESS
import com.bugevil.mhelmi.weatherforecast.utils.showErrorMessage
import com.bugevil.mhelmi.weatherforecast.utils.showLoading
import com.bugevil.mhelmi.weatherforecast.utils.viewmodel.ViewModelFactory
import com.bugevil.mhelmi.weatherforecast.utils.views.BaseFragment
import javax.inject.Inject

class CityWeatherFragment : BaseFragment<FragmentCityWeatherBinding>() {

  @Inject lateinit var viewModelFactory: ViewModelFactory
  private val weatherViewModel: WeatherViewModel by activityViewModels { viewModelFactory }
  private lateinit var checkBoxFavorite: AppCompatCheckBox
  private val weatherAdapter: WeatherAdapter by lazy { WeatherAdapter() }
  private val args: CityWeatherFragmentArgs by navArgs()

  companion object {
    @JvmStatic
    fun show(navController: NavController, city: City) {
      val action = CityWeatherFragmentDirections.actionNavigateToCityWeatherFragment(city)
      navController.navigate(action)
    }
  }

  override fun onBind(inflater: LayoutInflater, container: ViewGroup?): FragmentCityWeatherBinding {
    setHasOptionsMenu(true)
    appComponent.inject(this)
    return FragmentCityWeatherBinding.inflate(inflater, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupWeatherRecyclerView()
    getCityWeatherForecast()
  }

  private fun setupWeatherRecyclerView() {
    binding.rvWeatherForecast.layoutManager = LinearLayoutManager(context)
    binding.rvWeatherForecast.adapter = weatherAdapter
  }

  private fun getCityWeatherForecast() {
    weatherViewModel.getCityWeatherForecast(args.city.id)
      .observe(viewLifecycleOwner, this::handleWeatherListResult)
  }

  private fun handleWeatherListResult(result: Resource<out List<WeatherData>>) {
    when (result.resourceType) {
      LOADING -> setLoading(true)
      SUCCESS -> {
        setLoading(false)
        submitWeatherList(result.data)
      }
      else -> showError()
    }
  }

  private fun setLoading(isLoading: Boolean) = binding.layoutProgress.showLoading(isLoading)

  private fun submitWeatherList(list: List<WeatherData>?) = weatherAdapter.submitList(list)

  private fun showError() {
    binding.layoutProgress.showErrorMessage(getString(R.string.connection_error))
  }

  override fun onCreateOptionsMenu(
    menu: Menu,
    inflater: MenuInflater
  ) {
    inflater.inflate(R.menu.menu_city_weather, menu)
    val item: MenuItem = menu.findItem(R.id.action_favorite)
    checkBoxFavorite = item.actionView.findViewById(R.id.checkboxFavorite)
    checkBoxFavorite.isChecked = args.city.isFavorite
    checkBoxFavorite.setOnCheckedChangeListener { _, isChecked ->
      loge("checkBoxFavorite is checked = $isChecked")
      args.city.apply { isFavorite = isChecked }
        .also { weatherViewModel.updateCity(it) }
    }
    return super.onCreateOptionsMenu(menu, inflater)
  }
}