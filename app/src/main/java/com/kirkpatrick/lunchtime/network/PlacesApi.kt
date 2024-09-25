package com.kirkpatrick.lunchtime.network

import com.kirkpatrick.lunchtime.network.model.NearbySearchRequest
import com.kirkpatrick.lunchtime.network.model.PlacesSearchResponse
import com.kirkpatrick.lunchtime.network.model.TextSearchRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface PlacesApi {

    @POST("/v1/places:searchNearby")
    suspend fun searchNearby(@Body request: NearbySearchRequest) : PlacesSearchResponse

    @POST("/v1/places:searchText")
    suspend fun searchText(@Body request: TextSearchRequest) : PlacesSearchResponse

}