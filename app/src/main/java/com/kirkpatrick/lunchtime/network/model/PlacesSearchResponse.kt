package com.kirkpatrick.lunchtime.network.model

import com.google.gson.annotations.SerializedName

data class PlacesSearchResponse(
    @SerializedName("places") val places: List<Place>
)

data class Place(
    @SerializedName("id") val id: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("userRatingCount") val userRatingCount: Int,
    @SerializedName("priceLevel") val priceLevel: String? = null,
    @SerializedName("displayName") val displayName: DisplayName
)

data class DisplayName(
    @SerializedName("text") val text: String,
    @SerializedName("languageCode") val languageCode: String
)
