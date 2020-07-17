package com.bugevil.mhelmi.weatherforecast.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PrefsModule {
  @Singleton @Provides
  fun providePrefs(context: Context?): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
  }

  @Singleton @Provides
  fun providePrefsEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
    return sharedPreferences.edit()
  }
}