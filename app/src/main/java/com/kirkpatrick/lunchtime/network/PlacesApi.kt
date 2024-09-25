package com.kirkpatrick.lunchtime.network

import com.kirkpatrick.lunchtime.network.model.NearbySearchRequest
import com.kirkpatrick.lunchtime.network.model.NearbySearchResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PlacesApi {

    @POST("/v1/places:searchNearby")
    suspend fun searchNearby(@Body request: NearbySearchRequest) : NearbySearchResponse

}