package com.kirkpatrick.lunchtime.network

import com.kirkpatrick.lunchtime.network.model.LocationRestriction
import com.kirkpatrick.lunchtime.network.model.Place

interface PlacesRepository {
    suspend fun searchNearby(latitude: Double, longitude: Double): List<Place>

    suspend fun searchText(query: String): List<Place>
}
