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
    @SerializedName("displayName") val displayName: DisplayName,
    @SerializedName("location") val location: Location,
    @SerializedName("editorialSummary") val editorialSummary: EditorialSummary? = null,
    @SerializedName("formattedAddress") val formattedAddress: String
)

data class DisplayName(
    @SerializedName("text") val text: String,
    @SerializedName("languageCode") val languageCode: String
)

data class Location(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)

data class EditorialSummary(
    @SerializedName("languageCode") val languageCode: String,
    @SerializedName("text") val content: String
)
