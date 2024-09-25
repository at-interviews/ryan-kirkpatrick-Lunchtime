package com.kirkpatrick.lunchtime.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.launch
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kirkpatrick.lunchtime.R
import com.kirkpatrick.lunchtime.databinding.FragmentRestaurantMapBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestaurantMapFragment : Fragment(), OnMapReadyCallback {

    private val restaurantSearchViewModel by activityViewModels<RestaurantSearchViewModel>()
    private lateinit var binding : FragmentRestaurantMapBinding

    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantMapBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                restaurantSearchViewModel.restaurants.collect { places ->
                    if(places.isNotEmpty() && ::map.isInitialized) {
                        map.clear()
                        places.forEach { place ->
                            map.addMarker(
                                MarkerOptions()
                                    .position(LatLng(place.latitude, place.longitude))
                                    .title(place.name)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
                            )
                        }
                        map.animateCamera(CameraUpdateFactory.newLatLng(
                            LatLng(places.first().latitude, places.first().longitude))
                        )
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val initialLocation = restaurantSearchViewModel.restaurants.value.firstOrNull()?.let {
            LatLng(it.latitude, it.longitude)
        } ?: LatLng(32.7157, -117.161)

        restaurantSearchViewModel.restaurants.value.forEach {
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(it.latitude, it.longitude))
                    .title(it.name)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
            )
        }

        map.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 13f))
        }
    }
}