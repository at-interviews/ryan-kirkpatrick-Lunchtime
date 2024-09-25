package com.kirkpatrick.lunchtime

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.kirkpatrick.lunchtime.network.PlacesRepository
import com.kirkpatrick.lunchtime.network.model.Center
import com.kirkpatrick.lunchtime.network.model.Circle
import com.kirkpatrick.lunchtime.network.model.LocationRestriction
import com.kirkpatrick.lunchtime.network.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantSearchViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val bounds = CircularBounds.newInstance(
        LatLng(32.9892, -117.2724),
        10000.0
    )
    private val includedTypes = listOf("restaurant")

    private val _restaurants = MutableStateFlow<List<UiPlace>>(emptyList())
    val restaurants: StateFlow<List<UiPlace>> = _restaurants.asStateFlow()

    fun searchNearby() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val places = placesRepository.searchNearby(
                    includedTypes = listOf("restaurant"),
                    maxResultCount = 10,
                    locationRestriction = LocationRestriction(
                        Circle(
                            center = Center(
                                latitude = 32.9892,
                                longitude = -117.2724
                            ),
                            radius = 10000.0
                        )
                    )
                )

                places.map {
                    UiPlace(
                        id = it.id,
                        rating = it.rating,
                        userRatingCount = it.userRatingCount,
                        name = it.displayName.text,
                        priceLevel = when(it.priceLevel) {
                            "PRICE_LEVEL_INEXPENSIVE" -> 1
                            "PRICE_LEVEL_MODERATE" -> 2
                            "PRICE_LEVEL_EXPENSIVE" -> 3
                            else -> 0
                        })
                }.let {
                    _restaurants.value = it
                }
            } catch (exception: Exception) {
                Log.e("searchNearby", "Failed to search nearby", exception)
            }
        }
    }

    fun updateRestaurantsFromQuery(place: Place) {


    }

}

data class UiPlace(
    val id: String,
    val rating: Double,
    val userRatingCount: Int,
    val priceLevel: Int,
    val name: String
)