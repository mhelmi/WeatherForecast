package com.bugevil.mhelmi.weatherforecast.di

import com.bugevil.mhelmi.weatherforecast.features.home.data.CitiesRepositoryImpl
import com.bugevil.mhelmi.weatherforecast.features.home.domain.CitiesRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoriesModule {
  @Singleton @Binds
  abstract fun bindCitiesRepository(citiesRepository: CitiesRepositoryImpl?): CitiesRepository?
}