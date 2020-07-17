package com.bugevil.mhelmi.weatherforecast.utils

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
  private val sharedPreferences: SharedPreferences,
  private val editor: SharedPreferences.Editor
) {

  companion object {
    private const val SHOULD_POPULATE_CITIES = "should_populate_cities"
    private const val CAN_REQUEST_LOCATION = "can_request_location"
  }

  var shouldPopulateCities: Boolean
    get() = sharedPreferences.getBoolean(SHOULD_POPULATE_CITIES, true)
    set(isFistTime) {
      editor.putBoolean(SHOULD_POPULATE_CITIES, isFistTime).apply()
    }

  var canRequestLocation: Boolean
    get() = sharedPreferences.getBoolean(CAN_REQUEST_LOCATION, true)
    set(canRequestLocation) {
      editor.putBoolean(CAN_REQUEST_LOCATION, canRequestLocation).apply()
    }

  fun clearData() {
    editor.clear().apply()
  }
}