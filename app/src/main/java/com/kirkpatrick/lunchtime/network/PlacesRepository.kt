package com.kirkpatrick.lunchtime.network

import com.kirkpatrick.lunchtime.network.model.LocationRestriction
import com.kirkpatrick.lunchtime.network.model.Place

interface PlacesRepository {
    suspend fun searchNearby(locationRestriction: LocationRestriction): List<Place>

    suspend fun searchText(query: String): List<Place>
}
