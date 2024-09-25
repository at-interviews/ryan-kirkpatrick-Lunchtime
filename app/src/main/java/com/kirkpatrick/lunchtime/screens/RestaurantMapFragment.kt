package com.kirkpatrick.lunchtime.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.kirkpatrick.lunchtime.R
import com.kirkpatrick.lunchtime.databinding.FragmentRestaurantMapBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val fl = 13f

@AndroidEntryPoint
class RestaurantMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

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
                            val marker = map.addMarker(
                                MarkerOptions()
                                    .position(LatLng(place.latitude, place.longitude))
                                    .title(place.name)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))

                            )
                            marker?.tag = place.id
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
        } ?: LatLng(32.7157, -117.161) //Default to San Diego for fun

        restaurantSearchViewModel.restaurants.value.forEach {
            val marker = map.addMarker(
                MarkerOptions()
                    .position(LatLng(it.latitude, it.longitude))
                    .title(it.name)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
            )
            marker?.tag = it.id
        }

        map.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, DEFAULT_ZOOM))
            setOnInfoWindowClickListener(this@RestaurantMapFragment)
        }
    }

    override fun onInfoWindowClick(marker: Marker) {
        val id = marker.tag as String
        restaurantSearchViewModel.restaurants.value.find { it.id == id }?.let {
            val directions = RestaurantMapFragmentDirections
                .actionRestaurantMapFragmentToRestaurantDetailFragment(it)
            findNavController().navigate(directions)
        }
    }

    private companion object {
        const val DEFAULT_ZOOM = 13F
    }
}