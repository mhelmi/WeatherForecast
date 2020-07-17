package com.bugevil.mhelmi.weatherforecast.features.home.domain

import com.bugevil.mhelmi.weatherforecast.features.home.data.models.City
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.WeatherData
import com.bugevil.mhelmi.weatherforecast.utils.result.Resource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
  suspend fun populateCities()

  suspend fun updateCity(city: City)

  fun getAllCities(): Flow<List<City>>

  fun getFavoriteCities(): Flow<List<City>>

  fun searchForCity(query: String): Flow<List<City>>

  suspend fun setDefaultCity(cityName: String?, countryCode: String?): Int

  fun getCityWeatherById(cityId: Int): Flow<Resource<List<WeatherData>>>
}