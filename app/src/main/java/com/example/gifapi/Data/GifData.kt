package com.example.gifapi.Data

import com.google.gson.annotations.*

data class GifData(
    @SerializedName("id") val id: String,
    @SerializedName("images") val images: Images
)