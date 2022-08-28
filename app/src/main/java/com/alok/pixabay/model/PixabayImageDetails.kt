package com.alok.pixabay.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PixabayImageDetails(
    @SerializedName("id") val id: Int?,
    @SerializedName("tags") val tags: String?,
    @SerializedName("previewURL") val previewURL: String?,
    @SerializedName("largeImageURL") val largeImageURL: String?,
    @SerializedName("downloads") val downloads: Int?,
    @SerializedName("likes") val likes: Int?,
    @SerializedName("comments") val comments: Int?,
    @SerializedName("user") val user: String?,
): Serializable
