package com.kirkpatrick.lunchtime

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RestaurantSearchViewModel @Inject constructor(
    private val placesClient: PlacesClient
) : ViewModel() {

    private val placeFields = listOf(
        Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.RATING,
        Place.Field.USER_RATING_COUNT, Place.Field.PRICE_LEVEL)
    private val bounds = CircularBounds.newInstance(
        LatLng(32.9892, -117.2724),
        10000.0
    )
    private val includedTypes = listOf("restaurant")

    private val _restaurants = MutableStateFlow<List<Place>>(emptyList())
    val restaurants: StateFlow<List<Place>> = _restaurants.asStateFlow()

    fun searchNearby() {
        val nearbySearchRequest = SearchNearbyRequest.builder(bounds, placeFields)
            .setIncludedTypes(includedTypes)
            .setMaxResultCount(15)
            .build()

        placesClient.searchNearby(nearbySearchRequest)
            .addOnSuccessListener { response ->
                _restaurants.value = response.places //TODO Convert to Data class
            }
    }

    fun updateRestaurantsFromQuery(place: Place) {
        _restaurants.value = listOf(place)

    }

}