package com.kirkpatrick.lunchtime.network

import com.kirkpatrick.lunchtime.network.model.Center
import com.kirkpatrick.lunchtime.network.model.Circle
import com.kirkpatrick.lunchtime.network.model.LocationRestriction
import com.kirkpatrick.lunchtime.network.model.NearbySearchRequest
import com.kirkpatrick.lunchtime.network.model.Place
import com.kirkpatrick.lunchtime.network.model.TextSearchRequest

class DefaultPlacesRepository(
    private val placesApi: PlacesApi
) : PlacesRepository {

    override suspend fun searchNearby(latitude: Double, longitude: Double): List<Place> {
        return placesApi.searchNearby(
            NearbySearchRequest(
                includedTypes = listOf(INCLUDED_TYPES),
                maxResultCount = MAX_RESULT_COUNT,
                locationRestriction = LocationRestriction(
                    Circle(
                        center = Center(
                            latitude = latitude,
                            longitude = longitude
                        ),
                        radius = SEARCH_RADIUS
                    )
                )
            )
        ).places
    }

    override suspend fun searchText(query: String): List<Place> {
        return placesApi.searchText(TextSearchRequest(query)).places
    }

    private companion object {
        private const val MAX_RESULT_COUNT = 20
        private const val INCLUDED_TYPES = "restaurant"
        private const val SEARCH_RADIUS = 10000.0
    }
}