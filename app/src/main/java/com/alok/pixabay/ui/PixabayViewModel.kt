package com.alok.pixabay.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alok.pixabay.domain.PixabayUsecase
import com.alok.pixabay.model.PixabayImageDetails
import com.alok.pixabay.model.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PixabayViewModel @Inject constructor(
    private val pixabayUsecase: PixabayUsecase
): ViewModel() {

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage

    private val _apiLoaderLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val apiLoaderLiveData: LiveData<Boolean> = _apiLoaderLiveData

    private val _pixabayImageData: MutableLiveData<List<PixabayImageDetails>?> = MutableLiveData()
    val pixabayImageDataData: LiveData<List<PixabayImageDetails>?> = _pixabayImageData

    fun getPixabayImageData(queryText: String, page: Int) {
        _apiLoaderLiveData.value = true
        viewModelScope.launch {
            when(val result = pixabayUsecase.getPixabayImageData(queryText, page)){
                ResponseResult.Loading -> {}//ignore
                is ResponseResult.Error -> {
                    _apiLoaderLiveData.value = false
                    _errorMessage.value = result.errorMessage
                }
                is ResponseResult.Success -> {
                    _apiLoaderLiveData.value = false
                    _errorMessage.value = ""
                    _pixabayImageData.value = result.data
                }
            }
        }
    }
}