package com.bugevil.mhelmi.weatherforecast.utils

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.bugevil.mhelmi.weatherforecast.App
import com.bugevil.mhelmi.weatherforecast.di.AppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

val Context.app: App get() = applicationContext as App

/**
 * enable dagger AppComponent for any Context
 */
val Context.appComponent: AppComponent get() = app.appComponent

/**
 * enable dagger AppComponent for any Fragment
 */
val Fragment.appComponent: AppComponent
  get() = requireContext().app.appComponent

/**
 * convert search view text change to flow
 */
fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {
  val queryStateFlow = MutableStateFlow("")
  setOnQueryTextListener(object : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String): Boolean {
      queryStateFlow.value = query
      return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
      queryStateFlow.value = newText
      return true
    }
  })
  return queryStateFlow
}

inline fun <reified T> ArrayAdapter<T>.submitList(list: List<T>?) {
  clear()
  list?.let {
    addAll(it)
    notifyDataSetChanged()
  }
}

val ViewGroup.layoutInflater: LayoutInflater get() = LayoutInflater.from(this.context)

fun call(
  onError: ((Throwable) -> Unit)? = null,
  action: suspend () -> Unit
) {
  CoroutineScope(Dispatchers.Default).launch {
    try {
      action()
    } catch (e: Exception) {
      onError?.invoke(e)
    }
  }
}

/**
 * tag for any class
 */
inline val <reified T> T.TAG: String get() = T::class.java.simpleName

/**
 * log types enabled to any object
 */
inline fun <reified T> T.logv(message: String) = Log.v(TAG, message)
inline fun <reified T> T.loge(message: String) = Log.e(TAG, message)
inline fun <reified T> T.loge(message: String, throwable: Throwable) =
  Log.e(TAG, message, throwable)

inline fun <reified T> T.logd(message: String) = Log.d(TAG, message)
inline fun <reified T> T.logi(message: String) = Log.i(TAG, message)
inline fun <reified T> T.logw(message: String) = Log.w(TAG, message)
inline fun <reified T> T.logwtf(message: String) = Log.wtf(TAG, message)

/**
 * Format date-time info readable day-date-time
 */
fun String.toReadableDateTime(): String? {
  val oldFormat = SimpleDateFormat(Const.SERVER_DATE_TIME_FORMAT, Locale.getDefault())
  val newFormat = SimpleDateFormat(Const.DAY_DATE_TIME_FORMAT, Locale.getDefault())
  try {
    val date = oldFormat.parse(this)
    if (date != null) return newFormat.format(date)
  } catch (e: ParseException) {
    e.printStackTrace()
  }
  return null
}