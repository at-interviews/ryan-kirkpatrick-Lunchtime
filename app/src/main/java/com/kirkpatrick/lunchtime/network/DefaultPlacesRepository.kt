package com.kirkpatrick.lunchtime.network

import com.kirkpatrick.lunchtime.network.model.LocationRestriction
import com.kirkpatrick.lunchtime.network.model.NearbySearchRequest
import com.kirkpatrick.lunchtime.network.model.Place

class DefaultPlacesRepository(
    private val placesApi: PlacesApi
) : PlacesRepository {

    override suspend fun searchNearby(
        includedTypes: List<String>,
        maxResultCount: Int,
        locationRestriction: LocationRestriction
    ): List<Place> {
        return placesApi.searchNearby(
            NearbySearchRequest(
                includedTypes = includedTypes,
                maxResultCount = maxResultCount,
                locationRestriction = locationRestriction
            )
        ).places
    }
}