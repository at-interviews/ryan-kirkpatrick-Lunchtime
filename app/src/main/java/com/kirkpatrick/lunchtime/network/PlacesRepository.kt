package com.kirkpatrick.lunchtime.network

import android.location.Location
import com.kirkpatrick.lunchtime.network.model.Place

interface PlacesRepository {
    suspend fun searchNearby(latitude: Double, longitude: Double): List<Place>

    suspend fun searchText(query: String, location: Location?): List<Place>
}
