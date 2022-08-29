package com.alok.pixabay.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.alok.pixabay.data.room.PixabayDao
import com.alok.pixabay.data.room.PixabayDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataDependenciesModule {

    @Provides
    @Singleton
    fun getDb(@ApplicationContext applicationContext: Context): PixabayDatabase {
        return Room.databaseBuilder(
            applicationContext,
            PixabayDatabase::class.java, "pixabay-database"
        ).build()
    }

    @Provides
    @Singleton
    fun getPixabayDao(db: PixabayDatabase): PixabayDao {
        return db.pixabayDao()
    }

    @Provides
    @Singleton
    fun getSharedPref(@ApplicationContext applicationContext: Context): SharedPreferences {
        return applicationContext.getSharedPreferences("general", Context.MODE_PRIVATE)
    }

}

