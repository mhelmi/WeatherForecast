package com.bugevil.mhelmi.weatherforecast.features.home.domain

import javax.inject.Inject

class SetDefaultCityUseCase @Inject constructor(private val citiesRepository: CitiesRepository) {
  suspend fun execute(cityName: String?, countryCode: String?): Int =
    citiesRepository.setDefaultCity(cityName, countryCode)
}