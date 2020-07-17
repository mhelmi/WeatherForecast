package com.bugevil.mhelmi.weatherforecast.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import java.util.Locale

fun Location.getCityName(context: Context): String {
  val geoCoder = Geocoder(context, Locale.getDefault())
  val addresses: List<Address> = geoCoder.getFromLocation(this.latitude, this.longitude, 1)
  loge("getCityName: addresses = ${addresses[0]}")
  return addresses[0].adminArea
}

fun Location.getCityAndCountry(context: Context): Pair<String, String> {
  val geoCoder = Geocoder(context, Locale.getDefault())
  val addresses: List<Address> = geoCoder.getFromLocation(this.latitude, this.longitude, 1)
  val cityName: String = addresses[0].adminArea
  val countryCode = addresses[0].countryCode
  return Pair(cityName, countryCode)
}

@SuppressLint("MissingPermission")
fun Context.getLastKnownLocation(): Location? {
  val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
  val providers = locationManager.allProviders
  var bestLocation: Location? = null
  providers.forEach {
    val location: Location? = locationManager.getLastKnownLocation(it)
    logd("last known location, provider: $it, location: $location")
    if (location != null && (bestLocation == null || location.accuracy < bestLocation!!.accuracy)) {
      logd("found best last known location: $location")
      bestLocation = location
    }
  }
  return bestLocation
}