package com.bugevil.mhelmi.weatherforecast.features.home.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import com.google.gson.annotations.SerializedName

@Entity
@Keep
@Parcelize
data class City(
  @PrimaryKey @SerializedName("id") val id: Int,
  @SerializedName("name") val name: String? = null,
  @SerializedName("country") val country: String? = null,
  @SerializedName("state") val state: String? = null,
  @SerializedName("coord") @Embedded val coordinates: Coordinates? = null,
  var isFavorite: Boolean = false
) : Parcelable {
  override fun toString(): String {
    return if (name.isNullOrEmpty()) "" else "$name, " +
      if (state.isNullOrEmpty()) "" else "$state, " +
        if (country.isNullOrEmpty()) "" else "$country"
  }

  override fun equals(other: Any?): Boolean {
    if (other !is City) return false
    return id == other.id && name == other.name && country == other.country && state ==
      other.state && coordinates == other.coordinates && isFavorite == other.isFavorite
  }

  override fun hashCode(): Int {
    var result = id
    result = 31 * result + (name?.hashCode() ?: 0)
    result = 31 * result + (country?.hashCode() ?: 0)
    result = 31 * result + (state?.hashCode() ?: 0)
    result = 31 * result + (coordinates?.hashCode() ?: 0)
    result = 31 * result + isFavorite.hashCode()
    return result
  }
}

@Parcelize
@Keep
data class Coordinates(
  @SerializedName("lon") val lon: Double,
  @SerializedName("lat") val lat: Double
) : Parcelable {
  override fun equals(other: Any?): Boolean {
    if (other !is Coordinates) return false
    return lon == other.lon && lat == other.lat
  }

  override fun hashCode(): Int {
    var result = lon.hashCode()
    result = 31 * result + lat.hashCode()
    return result
  }
}

data class CityWithWeathers(
  @Embedded val city: City,
  @Relation(
    parentColumn = "id",
    entityColumn = "city_owner_id"
  )
  val weatherList: List<WeatherData>
)