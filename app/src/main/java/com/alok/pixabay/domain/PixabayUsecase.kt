package com.alok.pixabay.domain

import android.util.Log
import com.alok.pixabay.utillity.Constants.COMMON_ERROR_MESSAGE
import com.alok.pixabay.data.ApiService
import com.alok.pixabay.model.PixabayImageDetails
import com.alok.pixabay.model.ResponseResult
import javax.inject.Inject

class PixabayUsecase @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getPixabayImageData(
        queryText: String,
        page: Int
    ): ResponseResult<List<PixabayImageDetails>> {
        return try {
            val pixabayImageDetailsList = mutableListOf<PixabayImageDetails>()
            val data = apiService.getPixabayImageData(queryText, page)
            for (pixabayImageData in data.hits) {
                //converting tags into #tag format
                val tags = pixabayImageData.tags?.split(",") ?: mutableListOf()
                var requiredTags = ""
                for (tag in tags) {
                    requiredTags += "#$tag, "
                }
                requiredTags = requiredTags.substring(0, requiredTags.length - 1) //remove last comma

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
            }
            ResponseResult.Success(pixabayImageDetailsList)
        } catch (e: Exception) {
            Log.e("PixapayResponse", "getPixabayImageData exception: $e")
            ResponseResult.Error(COMMON_ERROR_MESSAGE)
        }
    }
}