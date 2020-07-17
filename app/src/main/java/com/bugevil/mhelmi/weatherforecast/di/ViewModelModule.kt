package com.bugevil.mhelmi.weatherforecast.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bugevil.mhelmi.weatherforecast.di.annotitions.ViewModelKey
import com.bugevil.mhelmi.weatherforecast.features.home.ui.viewmodels.WeatherViewModel
import com.bugevil.mhelmi.weatherforecast.utils.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
abstract class ViewModelModule {
  @Binds @IntoMap @ViewModelKey(WeatherViewModel::class)
  abstract fun provideWeatherViewModel(weatherViewModel: WeatherViewModel): ViewModel
}