package com.alok.pixabay.contract

interface ItemClickCallback<T> {
    fun onItemClicked(value: T, position: Int = 0)
}