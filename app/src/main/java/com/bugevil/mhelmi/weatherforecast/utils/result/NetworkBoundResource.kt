package com.bugevil.mhelmi.weatherforecast.utils.result

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.*
import java.io.IOException

/**
 * A generic class that can provide a resource backed by both the room database and the network.
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResourceType></ResourceType> */
abstract class NetworkBoundResource<ResourceType, ResponseType> {

  fun asFlow() = flow {
    emit(Resource.loading())
    val dbValue = loadFromDb()
      .map { mapLocalData(it) }
      .first()
    if (shouldFetch(dbValue)) {
      emit(Resource.loading(dbValue))
      try {
        val apiResponse = fetchFromNetwork()
        saveNetworkResult(apiResponse)
        emitAll(loadFromDb()
          .map { mapLocalData(it) }
          .map {
            if (it is List<*> && it.isEmpty()) {
              Resource.emptyDataError()
            } else {
              Resource.success(it)
            }
          })
      } catch (e: IOException) {
        onFetchFailed()
        emitAll(loadFromDb()
          .map { mapLocalData(it) }
          .map { Resource.error(it) })
      }
    } else {
      emitAll(
        loadFromDb()
          .map { mapLocalData(it) }
          .map {
            if (it is List<*> && it.isEmpty()) {
              Resource.emptyDataError()
            } else {
              Resource.success(it)
            }
          })
    }
  }

  protected open fun onFetchFailed() {
    // Implement in sub-classes to handle errors
  }

  open fun mapLocalData(resourceType: ResourceType): ResourceType {
    return resourceType
  }

  @MainThread
  protected abstract suspend fun fetchFromNetwork(): ResponseType

  @MainThread
  protected abstract fun loadFromDb(): Flow<ResourceType>

  @WorkerThread
  protected abstract suspend fun saveNetworkResult(response: ResponseType)

  @MainThread
  protected abstract fun shouldFetch(data: ResourceType): Boolean
}