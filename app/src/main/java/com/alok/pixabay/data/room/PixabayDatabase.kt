package com.alok.pixabay.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alok.pixabay.model.PixabayImageDetails

@Database(entities = [PixabayImageDetails::class], version = 1)
abstract class PixabayDatabase : RoomDatabase() {
    abstract fun pixabayDao(): PixabayDao
}