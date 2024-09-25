package com.kirkpatrick.lunchtime.network.model

import com.google.gson.annotations.SerializedName

data class TextSearchRequest(
    @SerializedName("textQuery") val textQuery: String
)