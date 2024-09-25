package com.kirkpatrick.lunchtime

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

//    private val bounds = CircularBounds.newInstance(
//        LatLng(32.9892, -117.2724),
//        10000.0
//    )

    private val _restaurants = MutableStateFlow<List<UiPlace>>(emptyList())
    val restaurants: StateFlow<List<UiPlace>> = _restaurants.asStateFlow()

    fun searchText(query: String) {
        if(query.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                placesRepository.searchText(query)
                    .map { it.toUiPlace() }.let { _restaurants.value = it }
            } catch (exception: Exception) {
                Log.e("textSearch", "Failed to search text", exception)
            }
        }
    }

    fun searchNearby() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val places = placesRepository.searchNearby(
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

                places.map { it.toUiPlace() }.let { _restaurants.value = it }

            } catch (exception: Exception) {
                Log.e("searchNearby", "Failed to search nearby", exception)
            }
        }
    }

    fun Place.toUiPlace() =
        UiPlace(
            id = this.id,
            rating = this.rating,
            userRatingCount = this.userRatingCount,
            name = this.displayName.text,
            priceLevel = when(this.priceLevel) {
                "PRICE_LEVEL_INEXPENSIVE" -> 1
                "PRICE_LEVEL_MODERATE" -> 2
                "PRICE_LEVEL_EXPENSIVE" -> 3
                else -> 0
            }
        )

}

data class UiPlace(
    val id: String,
    val rating: Double,
    val userRatingCount: Int,
    val priceLevel: Int,
    val name: String
)