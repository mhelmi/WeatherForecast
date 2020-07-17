package com.bugevil.mhelmi.weatherforecast.di

import android.content.Context
import androidx.room.Room
import com.bugevil.mhelmi.weatherforecast.features.home.data.local.CitiesDao
import com.bugevil.mhelmi.weatherforecast.features.home.data.local.WeathersDao
import com.bugevil.mhelmi.weatherforecast.utils.Const
import com.bugevil.mhelmi.weatherforecast.utils.db.MainRoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
  @Singleton @Provides
  fun providesRoomDatabase(context: Context): MainRoomDatabase {
    return Room.databaseBuilder(
      context.applicationContext, MainRoomDatabase::class.java, Const.KEY_DATABASE_NAME
    ).build()
  }

  @Singleton @Provides
  fun providePatientsDao(db: MainRoomDatabase) = db.citiesDao()

  @Singleton @Provides
  fun provideWeathersDao(db: MainRoomDatabase) = db.weathersDao()
}