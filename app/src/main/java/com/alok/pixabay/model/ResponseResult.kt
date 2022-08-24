package com.alok.pixabay.model

sealed class ResponseResult<out T> {
    object Loading: ResponseResult<Nothing>()
    data class Success<T>(val data: T?): ResponseResult<T>()
    data class Error(val errorMessage: String): ResponseResult<Nothing>()
}