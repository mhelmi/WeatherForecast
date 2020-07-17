package com.bugevil.mhelmi.weatherforecast.utils

import android.view.View
import com.bugevil.mhelmi.weatherforecast.databinding.LayoutProgressBinding

fun LayoutProgressBinding.showLoading(isLoading: Boolean, message: String? = null) {
  if (isLoading) startLoading(message) else stopLoading()
}

fun LayoutProgressBinding.startLoading(message: String? = null) {
  layoutBackgroundProgress.visibility = View.VISIBLE
  layoutLoading.visibility = View.VISIBLE
  tvMessage.visibility = View.GONE
  message?.let { tvLoadingMessage.text = it }
}

fun LayoutProgressBinding.stopLoading() {
  layoutBackgroundProgress.visibility = View.GONE
  layoutLoading.visibility = View.GONE
  tvMessage.visibility = View.GONE
}

fun LayoutProgressBinding.showErrorMessage(errorMessage: String) {
  layoutBackgroundProgress.visibility = View.VISIBLE
  layoutLoading.visibility = View.GONE
  tvMessage.visibility = View.VISIBLE
  tvMessage.text = errorMessage
}