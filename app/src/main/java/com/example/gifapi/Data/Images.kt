package com.example.gifapi.Data

import com.google.gson.annotations.*

data class ImageDetail(
    @SerializedName("url") val url: String
)