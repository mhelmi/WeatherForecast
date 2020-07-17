package com.bugevil.mhelmi.weatherforecast.features.home.domain

import javax.inject.Inject

class PopulateCitiesUseCase @Inject constructor(private val citiesRepository: CitiesRepository) {
    suspend fun execute() = citiesRepository.populateCities()
}