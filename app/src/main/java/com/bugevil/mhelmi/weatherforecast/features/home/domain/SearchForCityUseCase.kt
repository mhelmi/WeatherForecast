package com.bugevil.mhelmi.weatherforecast.features.home.domain

import com.bugevil.mhelmi.weatherforecast.features.home.data.models.City
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.WeatherData
import com.bugevil.mhelmi.weatherforecast.utils.result.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchForCityUseCase @Inject constructor(private val citiesRepository: CitiesRepository) {
  fun get(query: String): Flow<List<City>> = citiesRepository.searchForCity(query)
}