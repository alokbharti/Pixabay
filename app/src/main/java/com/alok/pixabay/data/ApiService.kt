package com.alok.pixabay.data

import com.alok.pixabay.BuildConfig
import com.alok.pixabay.model.PixabayApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/?key=${BuildConfig.PIXABAY_API_KEY}")
    suspend fun getPixabayImageData(
        @Query("q") queryText: String,
        @Query("page") page: Int
    ): PixabayApiResponse
}