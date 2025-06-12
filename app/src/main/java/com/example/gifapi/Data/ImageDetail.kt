package com.example.gifapi.Data

import com.google.gson.annotations.*

data class Images(
    @SerializedName("fixed_height") val preview: ImageDetail,
    @SerializedName("original") val fullSize: ImageDetail
)