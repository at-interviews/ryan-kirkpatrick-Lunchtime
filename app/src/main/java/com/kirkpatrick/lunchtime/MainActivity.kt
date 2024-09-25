package com.kirkpatrick.lunchtime

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kirkpatrick.lunchtime.databinding.MainActivityLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityLayoutBinding
    private val restaurantViewModel by viewModels<RestaurantSearchViewModel>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(hasLocationPermissions()) {
            getCurrentLocation()
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Handle the Enter key press here
                binding.searchEditText.text.toString()
                    .takeIf { it.isNotEmpty() }?.let {
                        restaurantViewModel.searchText(it)
                    }
                closeKeyboard()
                binding.searchEditText.clearFocus()
                true
            } else {
                false
            }
        }
        binding.searchLayoutWrapper.setEndIconOnClickListener {
            binding.searchEditText.text.toString()
                .takeIf { it.isNotEmpty() }?.let {
                    restaurantViewModel.searchText(it)
                }
            closeKeyboard()
            binding.searchEditText.clearFocus()
        }

    }

    private fun getCurrentLocation() {
        restaurantViewModel.setLoading(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val cancellationTokenSource = CancellationTokenSource() //TODO Cancel this if taking too long

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    restaurantViewModel.searchNearby(location)
                } else {
                    Log.e(TAG, "Current location is null")
                    getLastLocation()
                }
            }
            .addOnFailureListener { exception: Exception ->
                Log.e(TAG, "Failed to get current location", exception)
                getLastLocation()
            }
    }

    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if(location != null) {
                    restaurantViewModel.searchNearby(location)
                } else {
                    restaurantViewModel.setLoading(false)
                    Log.e(TAG, "Location is null")
                }
            }
    }

    private fun hasLocationPermissions() : Boolean {
        if(
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
            return false
        }

    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Log.i(TAG, "fine location granted")
                getCurrentLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                Log.i(TAG, "coarse location granted")
                getCurrentLocation()
            } else -> {
                Log.i(TAG, "No location access granted")
            }
        }
    }

    private fun closeKeyboard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
