package com.bugevil.mhelmi.weatherforecast.features.home.data.converters

import androidx.room.TypeConverter
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherListConverter {
  private val type = object : TypeToken<List<Weather>>() {}.type

  @TypeConverter
  fun toJson(obj: List<Weather>): String? = Gson().toJson(obj, type)

  @TypeConverter
  fun toList(json: String?): List<Weather> = Gson().fromJson<List<Weather>>(json, type)
}
