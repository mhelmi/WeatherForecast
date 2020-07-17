package com.bugevil.mhelmi.weatherforecast.features.home.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.*
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
@Keep
data class WeatherData(
  @PrimaryKey(autoGenerate = true) val id: Int,
  @ColumnInfo(name = "city_owner_id") var cityOwnerId: Int,
  @SerializedName("main") @Embedded val main: Main,
  @SerializedName("weather") val weatherList: List<Weather>,
  @SerializedName("dt_txt") val date: String
) : Parcelable {
  override fun equals(other: Any?): Boolean {
    if (other !is WeatherData) return false
    return id == other.id && cityOwnerId == other.cityOwnerId && main == other.main
      && weatherList == other.weatherList && date == other.date
  }

  override fun hashCode(): Int {
    var result = id
    result = 31 * result + cityOwnerId
    result = 31 * result + main.hashCode()
    result = 31 * result + weatherList.hashCode()
    result = 31 * result + date.hashCode()
    return result
  }
}

@Parcelize
@Keep
data class Main(
  @SerializedName("temp") val temp: Double,
  @SerializedName("temp_min") val tempMin: Double,
  @SerializedName("temp_max") val tempMax: Double,
  @SerializedName("pressure") val pressure: Double,
  @SerializedName("sea_level") val seaLevel: Double,
  @SerializedName("grnd_level") val grndLevel: Double,
  @SerializedName("humidity") val humidity: Int,
  @SerializedName("temp_kf") val tempKf: Double
) : Parcelable {
  override fun equals(other: Any?): Boolean {
    if (other !is Main) return false
    return temp == other.tempMin && tempMax == other.tempMax && pressure == other.pressure
      && seaLevel == other.seaLevel && grndLevel == other.grndLevel
      && humidity == other.humidity && tempKf == other.tempKf
  }

  override fun hashCode(): Int {
    var result = temp.hashCode()
    result = 31 * result + tempMin.hashCode()
    result = 31 * result + tempMax.hashCode()
    result = 31 * result + pressure.hashCode()
    result = 31 * result + seaLevel.hashCode()
    result = 31 * result + grndLevel.hashCode()
    result = 31 * result + humidity
    result = 31 * result + tempKf.hashCode()
    return result
  }
}

@Parcelize
@Keep
data class Weather(
  @SerializedName("id") val id: Int,
  @SerializedName("main") val main: String,
  @SerializedName("description") val description: String,
  @SerializedName("icon") val icon: String
) : Parcelable {
  override fun toString() = description

  override fun equals(other: Any?): Boolean {
    if (other !is Weather) return false
    return main == other.main && description == other.description && icon == other.icon
  }

  override fun hashCode(): Int {
    var result = id
    result = 31 * result + main.hashCode()
    result = 31 * result + description.hashCode()
    result = 31 * result + icon.hashCode()
    return result
  }
}