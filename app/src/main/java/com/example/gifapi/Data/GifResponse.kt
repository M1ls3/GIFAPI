package com.example.gifapi.Data

import com.google.gson.annotations.*

data class GifResponse(
    @SerializedName("data") val gifs: List<GifData>
)