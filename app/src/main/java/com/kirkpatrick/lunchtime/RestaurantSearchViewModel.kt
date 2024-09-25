package com.kirkpatrick.lunchtime

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirkpatrick.lunchtime.network.PlacesRepository
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

    private val _restaurants = MutableStateFlow<List<UiPlace>>(emptyList())
    val restaurants: StateFlow<List<UiPlace>> = _restaurants.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun searchText(query: String) {
        if(query.isEmpty()) return
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                placesRepository.searchText(query)
                    .map { it.toUiPlace() }.let { _restaurants.value = it }
                _loading.value = false
            } catch (exception: Exception) {
                Log.e("textSearch", "Failed to search text", exception)
            }
            _loading.value = false
        }
    }

    fun searchNearby(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                placesRepository.searchNearby(location.latitude, location.longitude)
                    .map { it.toUiPlace() }
                    .let { _restaurants.value = it }
            } catch (exception: Exception) {
                Log.e("searchNearby", "Failed to search nearby", exception)
            }
            _loading.value = false
        }
    }

    private fun Place.toUiPlace() =
        UiPlace(
            id = this.id,
            rating = this.rating,
            userRatingCount = this.userRatingCount,
            name = this.displayName.text,
            priceLevel = when(this.priceLevel) {
                "PRICE_LEVEL_INEXPENSIVE" -> 1
                "PRICE_LEVEL_MODERATE" -> 2
                "PRICE_LEVEL_EXPENSIVE" -> 3
                "PRICE_LEVEL_VERY_EXPENSIVE" -> 4
                else -> 0
            }
        )

    fun setLoading(loading: Boolean) { _loading.value = loading }

}

data class UiPlace(
    val id: String,
    val rating: Double,
    val userRatingCount: Int,
    val priceLevel: Int,
    val name: String
)