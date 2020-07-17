package com.bugevil.mhelmi.weatherforecast.features.home.data.local

import androidx.room.Dao
import androidx.room.Query
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.City
import com.bugevil.mhelmi.weatherforecast.utils.room.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CitiesDao : BaseDao<City> {
  @Query("SELECT * FROM city")
  abstract fun getAllCities(): Flow<List<City>>

  @Query("SELECT * FROM city WHERE isFavorite = 1 LIMIT 5")
  abstract fun getFavoriteCities(): Flow<List<City>>

  @Query("SELECT * FROM city WHERE name LIKE '%'||:query||'%' or state LIKE '%'||:query||'%' or country LIKE '%'||:query||'%' LIMIT 1000")
  abstract fun searchForCity(query: String): Flow<List<City>>

  @Query("UPDATE city SET isFavorite = 1 WHERE id = (SELECT id FROM city WHERE name LIKE '%'||:cityName||'%' and country LIKE '%'||:countryCode||'%' LIMIT 1)")
  abstract fun setDefaultCity(cityName: String?, countryCode: String?): Int
}