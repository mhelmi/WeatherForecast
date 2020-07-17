package com.bugevil.mhelmi.weatherforecast.features.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugevil.mhelmi.weatherforecast.R
import com.bugevil.mhelmi.weatherforecast.databinding.FragmentSearchCityBinding
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.City
import com.bugevil.mhelmi.weatherforecast.features.home.ui.adapters.CitiesAdapter
import com.bugevil.mhelmi.weatherforecast.features.home.ui.adapters.CitiesAdapter.CityActionsListener
import com.bugevil.mhelmi.weatherforecast.features.home.ui.viewmodels.WeatherViewModel
import com.bugevil.mhelmi.weatherforecast.utils.appComponent
import com.bugevil.mhelmi.weatherforecast.utils.getQueryTextChangeStateFlow
import com.bugevil.mhelmi.weatherforecast.utils.result.Resource
import com.bugevil.mhelmi.weatherforecast.utils.result.ResourceType
import com.bugevil.mhelmi.weatherforecast.utils.showErrorMessage
import com.bugevil.mhelmi.weatherforecast.utils.showLoading
import com.bugevil.mhelmi.weatherforecast.utils.viewmodel.ViewModelFactory
import com.bugevil.mhelmi.weatherforecast.utils.views.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchCityFragment : BaseFragment<FragmentSearchCityBinding>(), CityActionsListener {

  private lateinit var searchView: SearchView
  @Inject lateinit var viewModelFactory: ViewModelFactory
  private val weatherViewModel: WeatherViewModel by activityViewModels { viewModelFactory }
  private val citiesAdapter: CitiesAdapter by lazy { CitiesAdapter(this) }
  private var searchQuery: String? = null
  private var searchResultLiveData: LiveData<Resource<List<City>>>? = null

  companion object {
    const val SEARCH_QUERY = "search_query"

    @JvmStatic
    fun show(navController: NavController) =
      navController.navigate(R.id.action_navigate_to_searchCityFragment)
  }

  override fun onBind(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchCityBinding {
    appComponent.inject(this)
    setHasOptionsMenu(true)
    return FragmentSearchCityBinding.inflate(inflater, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupSearchResultRecyclerView()
  }

  private fun setupSearchResultRecyclerView() {
    binding.rvSearchCity.layoutManager = LinearLayoutManager(context)
    binding.rvSearchCity.adapter = citiesAdapter
  }

  private fun searchForCity(query: String) {
    searchResultLiveData?.let {
      if (it.hasActiveObservers()) it.removeObservers(viewLifecycleOwner)
    }
    searchResultLiveData = weatherViewModel.searchForCity(query)
    searchResultLiveData?.observe(viewLifecycleOwner, this@SearchCityFragment::handleSearchResult)
  }

  private fun handleSearchResult(result: Resource<List<City>>) {
    when (result.resourceType) {
      ResourceType.LOADING -> setLoading(true)
      ResourceType.SUCCESS -> {
        setLoading(false)
        submitSearchResultList(result.data)
      }
      ResourceType.EMPTY_DATA -> showError(getString(R.string.error_no_search_cities_found))
      else -> showError(getString(R.string.connection_error))
    }
  }

  private fun setLoading(isLoading: Boolean) = binding.layoutProgress.showLoading(isLoading)

  private fun submitSearchResultList(list: List<City>?) = citiesAdapter.submitList(list)

  private fun showError(msg: String) = binding.layoutProgress.showErrorMessage(msg)

  private fun setSearchViewObserver() {
    lifecycleScope.launch {
      searchView.getQueryTextChangeStateFlow()
        .debounce(400)
        .filter { query ->
          searchQuery = query
          if (query.isEmpty()) {
            submitSearchResultList(null)
            false
          } else query.length >= 2
        }
        .distinctUntilChanged()
        .collectLatest { searchForCity(it) }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_search_city, menu)
    val searchItem: MenuItem = menu.findItem(R.id.action_search)
    searchView = searchItem.actionView as SearchView
    setSearchViewObserver()
    searchView.queryHint = getString(R.string.search_for_cities)
    searchItem.expandActionView()
    searchView.setQuery(searchQuery, true)
    searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
      override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        return true
      }

      override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        navController.popBackStack()
        return true
      }
    })
    return super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putString(SEARCH_QUERY, searchQuery)
    super.onSaveInstanceState(outState)
  }

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    savedInstanceState?.getString(SEARCH_QUERY)?.let { searchQuery = it }
  }

  override fun onCityClick(city: City) {
    CityWeatherFragment.show(navController, city)
  }

  override fun onUpdateCityFavorite(city: City) {
    weatherViewModel.updateCity(city)
  }
}