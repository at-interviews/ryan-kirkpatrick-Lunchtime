package com.kirkpatrick.lunchtime

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantSearchViewModel @Inject constructor(
    private val placesClient: PlacesClient
) : ViewModel() {

    private val placeFields = listOf(Place.Field.ID, Place.Field.DISPLAY_NAME)
    private val bounds = CircularBounds.newInstance(
        LatLng(32.9892, -117.2724),
        10000.0
    )
    private val includedTypes = listOf("restaurant")

    fun searchNearby() {
        val nearbySearchRequest = SearchNearbyRequest.builder(bounds, placeFields)
            .setIncludedTypes(includedTypes)
            .setMaxResultCount(15)
            .build()

        placesClient.searchNearby(nearbySearchRequest)
            .addOnSuccessListener { response ->
                response.places.forEach {
                    Log.i("RestaurantSearchViewModel", it.displayName ?: "No display name")
                }
            }
    }

}