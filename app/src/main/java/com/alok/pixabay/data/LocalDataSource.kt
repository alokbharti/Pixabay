package com.alok.pixabay.data

import android.content.SharedPreferences
import androidx.room.Query
import com.alok.pixabay.data.room.PixabayDao
import com.alok.pixabay.model.PixabayImageDetails
import com.alok.pixabay.utillity.Constants
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class LocalDataSource @Inject constructor(
    private val dao: PixabayDao,
    private val sharedPref: SharedPreferences
) {
    suspend fun insertPixabayImageDetailsList(pixabayImageList: List<PixabayImageDetails>) {
        dao.insertPixabayImageDetailsList(pixabayImageList)
    }

    suspend fun getAllPixabayImageData(): List<PixabayImageDetails> {
        return dao.getAllPixabayImageData()
    }

    suspend fun deleteExistingPixabayImageData() {
        dao.deleteExistingPixabayImageData()
    }

    fun saveLastSearchedQueryToSharedPref(value: String) {
        val editor = sharedPref.edit()
        editor.putString(Constants.LAST_SEARCHED_QUERY, value)
        editor.apply()
    }

    fun getLastSearchedQueryFromSharedPref(): String {
        return sharedPref.getString(
            Constants.LAST_SEARCHED_QUERY,
            Constants.DEFAULT_QUERY
        ) ?: Constants.DEFAULT_QUERY
    }
}