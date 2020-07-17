package com.bugevil.mhelmi.weatherforecast.features.home.data.remote

import com.bugevil.mhelmi.weatherforecast.BuildConfig
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.GetWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
  @GET("data/2.5/forecast")
  suspend fun getCityWeather(
    @Query("id") cityId: Int? = null,
    @Query("lat") latitude: Double? = null,
    @Query("lon") longitude: Double? = null,
    @Query("units") units: String = "metric",
    @Query("appid") appKey: String = BuildConfig.WEATHER_API_KEY
  ): GetWeatherResponse
}