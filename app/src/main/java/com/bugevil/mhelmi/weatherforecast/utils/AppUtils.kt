package com.bugevil.mhelmi.weatherforecast.utils

import android.content.Context
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.City
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object AppUtils {
  fun getCitiesFromAssets(context: Context): List<City> {
    val json = try {
      context.assets.open("city_list.json").bufferedReader().use { it.readText() }
    } catch (e: IOException) {
      e.printStackTrace()
      return arrayListOf()
    }
    val type =
      object : TypeToken<List<City>>() {}.type
    return Gson().fromJson(json, type)
  }
}