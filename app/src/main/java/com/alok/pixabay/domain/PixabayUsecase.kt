package com.alok.pixabay.domain

import android.content.SharedPreferences
import com.alok.pixabay.utillity.Constants.COMMON_ERROR_MESSAGE
import com.alok.pixabay.data.ApiService
import com.alok.pixabay.data.room.PixabayDao
import com.alok.pixabay.model.PixabayImageDetails
import com.alok.pixabay.model.ResponseResult
import com.alok.pixabay.utillity.Constants.DEFAULT_QUERY
import com.alok.pixabay.utillity.Constants.LAST_SEARCHED_QUERY
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class PixabayUsecase @Inject constructor(
    private val apiService: ApiService,
    private val dao: PixabayDao,
    private val sharedPref: SharedPreferences
) {

    private var lastSearchQuery = ""

    suspend fun getPixabayImageData(
        queryText: String,
        page: Int
    ): ResponseResult<List<PixabayImageDetails>> {
        return try {
            val pixabayImageDetailsList = mutableListOf<PixabayImageDetails>()
            val data = apiService.getPixabayImageData(queryText, page)
            data?.let {
                for (pixabayImageData in it.hits) {
                    //converting tags into #tag format
                    val tags = pixabayImageData.tags?.split(",") ?: mutableListOf()
                    var requiredTags = ""
                    for (tag in tags) {
                        requiredTags += "#${tag.trim()}, "
                    }
                    requiredTags =
                        requiredTags.substring(0, requiredTags.length - 2) //remove last comma

                    val pixabayImageDetails = PixabayImageDetails(
                        id = pixabayImageData.id,
                        tags = requiredTags,
                        previewURL = pixabayImageData.previewURL,
                        largeImageURL = pixabayImageData.largeImageURL,
                        downloads = pixabayImageData.downloads,
                        likes = pixabayImageData.likes,
                        comments = pixabayImageData.comments,
                        user = pixabayImageData.user
                    )
                    pixabayImageDetailsList.add(pixabayImageDetails)

                    if (page == 1 && pixabayImageDetailsList.size > 0){
                        //saving data for most recent search
                        withContext(Dispatchers.IO) {
                            dao.deleteExistingPixabayImageData()
                            dao.insertPixabayImageDetailsList(pixabayImageDetailsList)

                            //save last searched query to shared pref
                            val editor = sharedPref.edit()
                            editor.putString(LAST_SEARCHED_QUERY, queryText)
                            editor.apply()
                        }
                    }
                    lastSearchQuery = queryText
                }
                ResponseResult.Success(pixabayImageDetailsList)
            } ?: kotlin.run {
                getPixabayImageDataFromDb(page)
            }
        } catch (e: Exception) {
            getPixabayImageDataFromDb(page)
        }
    }

    /**
     * provides data from db if the first api call fails due to any reason.
     * To get more data, user should be connected to internet.
     * */
    private suspend fun getPixabayImageDataFromDb(page: Int): ResponseResult<List<PixabayImageDetails>> {
        val dataFromDb = dao.getAllPixabayImageData()
        return if (dataFromDb.isNotEmpty() && page == 1) {
            lastSearchQuery = sharedPref.getString(LAST_SEARCHED_QUERY, DEFAULT_QUERY) ?: DEFAULT_QUERY
            ResponseResult.Success(dataFromDb)
        } else {
            ResponseResult.Error(COMMON_ERROR_MESSAGE)
        }
    }

    fun getLastSearchedQuery() = lastSearchQuery
}