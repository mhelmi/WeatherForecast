package com.bugevil.mhelmi.weatherforecast.utils.result

import androidx.annotation.Keep
import com.bugevil.mhelmi.weatherforecast.utils.result.ResourceType.AUTH_FAILED_ERROR
import com.bugevil.mhelmi.weatherforecast.utils.result.ResourceType.CONNECTION_ERROR
import com.bugevil.mhelmi.weatherforecast.utils.result.ResourceType.EMPTY_DATA
import com.bugevil.mhelmi.weatherforecast.utils.result.ResourceType.ERROR
import com.bugevil.mhelmi.weatherforecast.utils.result.ResourceType.LOADING
import com.bugevil.mhelmi.weatherforecast.utils.result.ResourceType.SUCCESS
import com.bugevil.mhelmi.weatherforecast.utils.result.ResourceType.VALIDATION_ERROR

@Keep
class Resource<T> {
  val resourceType: ResourceType
  var data: T? = null
    private set
  var message: String? = null
    private set

  constructor(resourceType: ResourceType, data: T?, message: String?) {
    this.resourceType = resourceType
    this.data = data
    this.message = message
  }

  constructor(resourceType: ResourceType, data: T?) {
    this.resourceType = resourceType
    this.data = data
  }

  constructor(resourceType: ResourceType, message: String?) {
    this.resourceType = resourceType
    this.message = message
  }

  companion object {
    fun <T> success(): Resource<T> = Resource(SUCCESS, null)

    fun <T> success(data: T): Resource<T> = Resource(SUCCESS, data)

    fun <T> success(data: T, message: String?): Resource<T> = Resource(SUCCESS, data, message)

    fun <T> successMessage(message: String?): Resource<T> = Resource(SUCCESS, message)

    fun <T> error(message: String?, data: T): Resource<T> = Resource(ERROR, data, message)

    fun <T> error(message: String?): Resource<T> = Resource(ERROR, message)

    fun <T> error(data: T): Resource<T> = Resource(ERROR, data)

    fun <T> error(): Resource<T> = Resource(ERROR, null)

    fun <T> connectionError(): Resource<T> = Resource(CONNECTION_ERROR, null)

    fun <T> emptyDataError(): Resource<T> = Resource(EMPTY_DATA, null)

    fun <T> validationError(data: T, message: String?): Resource<T> =
      Resource(VALIDATION_ERROR, data, message)

    fun <T> validationError(data: T): Resource<T> = Resource(VALIDATION_ERROR, data)

    fun <T> authFailedError(data: T): Resource<T> = Resource(AUTH_FAILED_ERROR, data)

    fun <T> authFailedError(): Resource<T> = Resource(AUTH_FAILED_ERROR, null)

    fun <T> loading(): Resource<T> = Resource(LOADING, null)

    fun <T> loading(data: T): Resource<T> = Resource(LOADING, data)
  }
}