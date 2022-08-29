package com.alok.pixabay.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class PixabayImageDetails(
    @SerializedName("timestamp")
    @PrimaryKey
    val timestamp: Long = System.currentTimeMillis(),
    @SerializedName("id") val id: Int?,
    @SerializedName("tags") val tags: String?,
    @SerializedName("previewURL") val previewURL: String?,
    @SerializedName("largeImageURL") val largeImageURL: String?,
    @SerializedName("downloads") val downloads: Int?,
    @SerializedName("likes") val likes: Int?,
    @SerializedName("comments") val comments: Int?,
    @SerializedName("user") val user: String?,
): Serializable
