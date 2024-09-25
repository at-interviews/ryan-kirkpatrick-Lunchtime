package com.kirkpatrick.lunchtime.network.model

import com.google.gson.annotations.SerializedName

data class TextSearchRequest(
    @SerializedName("textQuery") val textQuery: String,
    @SerializedName("locationBias") val locationBias: LocationBias? = null
)

data class LocationBias(
    @SerializedName("circle") val circle: CircleBias
)

data class CircleBias(
    @SerializedName("center") val center: CenterBias,
    @SerializedName("radius") val radius: Double
)

data class CenterBias(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)