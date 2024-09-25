package com.kirkpatrick.lunchtime.network.model

import com.google.gson.annotations.SerializedName

data class NearbySearchRequest(
    @SerializedName("includedTypes") val includedTypes: List<String>,
    @SerializedName("maxResultCount") val maxResultCount: Int,
    @SerializedName("locationRestriction") val locationRestriction: LocationRestriction
)

data class LocationRestriction(
    @SerializedName("circle") val circle: Circle
)

data class Circle(
    @SerializedName("center") val center: Center,
    @SerializedName("radius") val radius: Double
)

data class Center(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)
