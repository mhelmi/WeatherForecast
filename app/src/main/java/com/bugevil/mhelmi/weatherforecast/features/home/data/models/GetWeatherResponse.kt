package com.bugevil.mhelmi.weatherforecast.features.home.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class GetWeatherResponse(
  @SerializedName("cod") val cod: String,
  @SerializedName("message") val message: Double,
  @SerializedName("cnt") val cnt: Int,
  @SerializedName("list") val weatherDataList: List<WeatherData>,
  @SerializedName("city") val city: City
) : Parcelable