package com.alok.pixabay.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alok.pixabay.model.PixabayImageDetails

@Dao
interface PixabayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPixabayImageDetailsList(pixabayImageList: List<PixabayImageDetails>)

    @Query("SELECT * FROM pixabayimagedetails")
    suspend fun getAllPixabayImageData(): List<PixabayImageDetails>

    @Query("DELETE FROM PixabayImageDetails")
    suspend fun deleteExistingPixabayImageData()
}