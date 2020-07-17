package com.bugevil.mhelmi.weatherforecast.features.home.domain

import com.bugevil.mhelmi.weatherforecast.features.home.data.models.City
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteCitiesUseCase @Inject constructor(private val citiesRepository: CitiesRepository) {
    fun get(): Flow<List<City>> = citiesRepository.getFavoriteCities()
}