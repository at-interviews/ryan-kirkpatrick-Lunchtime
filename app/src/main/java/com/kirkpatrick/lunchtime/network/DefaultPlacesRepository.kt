package com.kirkpatrick.lunchtime.network

import com.kirkpatrick.lunchtime.network.model.LocationRestriction
import com.kirkpatrick.lunchtime.network.model.NearbySearchRequest
import com.kirkpatrick.lunchtime.network.model.Place

class DefaultPlacesRepository(
    private val placesApi: PlacesApi
) : PlacesRepository {

    override suspend fun searchNearby(locationRestriction: LocationRestriction): List<Place> {
        return placesApi.searchNearby(
            NearbySearchRequest(
                includedTypes = listOf(INCLUDED_TYPES),
                maxResultCount = MAX_RESULT_COUNT,
                locationRestriction = locationRestriction
            )
        ).places
    }

    private companion object {
        private const val MAX_RESULT_COUNT = 20
        private const val INCLUDED_TYPES = "restaurant"
    }
}