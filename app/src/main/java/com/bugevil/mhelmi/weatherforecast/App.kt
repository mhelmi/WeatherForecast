package com.bugevil.mhelmi.weatherforecast

import android.app.Application
import com.bugevil.mhelmi.weatherforecast.di.AppComponent
import com.bugevil.mhelmi.weatherforecast.di.ContextModule
import com.bugevil.mhelmi.weatherforecast.di.DaggerAppComponent

class App : Application() {
  lateinit var appComponent: AppComponent
    private set

  override fun onCreate() {
    super.onCreate()
    appComponent = DaggerAppComponent.factory()
      .create(ContextModule(this))
  }
}