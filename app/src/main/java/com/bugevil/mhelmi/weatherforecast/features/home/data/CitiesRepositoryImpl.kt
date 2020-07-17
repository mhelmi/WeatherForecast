package com.bugevil.mhelmi.weatherforecast.features.home.data

import android.content.Context
import com.bugevil.mhelmi.weatherforecast.features.home.data.local.CitiesDao
import com.bugevil.mhelmi.weatherforecast.features.home.data.local.WeathersDao
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.City
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.GetWeatherResponse
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.WeatherData
import com.bugevil.mhelmi.weatherforecast.features.home.data.remote.WeatherApiService
import com.bugevil.mhelmi.weatherforecast.features.home.domain.CitiesRepository
import com.bugevil.mhelmi.weatherforecast.utils.AppUtils
import com.bugevil.mhelmi.weatherforecast.utils.result.NetworkBoundResource
import com.bugevil.mhelmi.weatherforecast.utils.result.FetchLimiter
import com.bugevil.mhelmi.weatherforecast.utils.result.Resource
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CitiesRepositoryImpl @Inject constructor(
  private val citiesDao: CitiesDao,
  private val weathersDao: WeathersDao,
  private val weatherApiService: WeatherApiService,
  private val context: Context
) : CitiesRepository {
  var fetchLimiter: FetchLimiter<Int> = FetchLimiter(1, TimeUnit.MINUTES)

  override suspend fun populateCities() {
    println("populateCities called")
    citiesDao.insert(AppUtils.getCitiesFromAssets(context))
  }

  override suspend fun setDefaultCity(cityName: String?, countryCode: String?): Int {
    return citiesDao.setDefaultCity(cityName, countryCode)
  }

  override suspend fun updateCity(city: City) {
    citiesDao.insert(city)
  }

  override fun getAllCities(): Flow<List<City>> {
    return citiesDao.getAllCities()
  }

  override fun getFavoriteCities(): Flow<List<City>> {
    return citiesDao.getFavoriteCities()
  }

  override fun searchForCity(query: String): Flow<List<City>> {
    return citiesDao.searchForCity(query)
  }

  override fun getCityWeatherById(cityId: Int): Flow<Resource<List<WeatherData>>> {
    return object : NetworkBoundResource<List<WeatherData>, GetWeatherResponse>() {
      override suspend fun fetchFromNetwork(): GetWeatherResponse {
        return weatherApiService.getCityWeather(cityId)
      }

      override fun loadFromDb(): Flow<List<WeatherData>> {
        return weathersDao.getWeathersByCityId(cityId)
      }

      override suspend fun saveNetworkResult(response: GetWeatherResponse) {
        response.weatherDataList.map { it.cityOwnerId = cityId }
        return weathersDao.updateList(cityId, response.weatherDataList)
      }

      override fun shouldFetch(data: List<WeatherData>): Boolean {
        return data.isEmpty() || fetchLimiter.shouldFetch(cityId)
      }

      override fun onFetchFailed() {
        fetchLimiter.reset(cityId)
      }
    }.asFlow()
  }
}

