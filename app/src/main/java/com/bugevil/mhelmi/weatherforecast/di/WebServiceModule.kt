package com.bugevil.mhelmi.weatherforecast.di

import com.bugevil.mhelmi.weatherforecast.BuildConfig
import com.bugevil.mhelmi.weatherforecast.di.annotitions.MainRetrofit
import com.bugevil.mhelmi.weatherforecast.features.home.data.remote.WeatherApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class WebServiceModule {
  @Singleton @Provides
  fun provideOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
      .connectTimeout(1, TimeUnit.MINUTES)
      .readTimeout(1, TimeUnit.MINUTES)
      .addInterceptor(logging)
      .build()
  }

  private fun buildRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(okHttpClient)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  @Singleton @Provides @MainRetrofit
  fun provideMainRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return buildRetrofit(okHttpClient, BuildConfig.MAIN_URL)
  }

  @Provides @Singleton
  fun provideWeatherApiService(@MainRetrofit retrofit: Retrofit): WeatherApiService {
    return retrofit.create(WeatherApiService::class.java)
  }
}