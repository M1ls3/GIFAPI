package com.example.gifapi.Retrofit

import com.example.gifapi.Data.GifResponse
import retrofit2.http.*

interface GifAPI {
    @GET("gifs/trending")
    suspend fun getTrendingGifs(
        @Query("api_key") apiKey: String = "YOUR_API_KEY", // Зарегистрируйтесь на GIPHY
        @Query("limit") limit: Int = 20
    ): GifResponse
}