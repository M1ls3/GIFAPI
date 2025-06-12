package com.example.gifapi.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifapi.Data.GifData
import com.example.gifapi.Retrofit.GifAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed class UiState {
    object Loading : UiState()
    data class Success(val gifs: List<GifData>) : UiState()
    object Error : UiState()
}

class MainViewModel : ViewModel() {
    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.giphy.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GifAPI::class.java)

    init {
        loadGifs()
    }

    private fun loadGifs() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    retrofit.getTrendingGifs()
                }
                _uiState.value = UiState.Success(response.gifs)
            } catch (e: Exception) {
                _uiState.value = UiState.Error
            }
        }
    }
}