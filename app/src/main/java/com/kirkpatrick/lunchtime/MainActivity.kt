package com.kirkpatrick.lunchtime

import android.R.attr.data
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.kirkpatrick.lunchtime.databinding.MainActivityLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPlacesApi()

        binding.searchEditText.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) { startAutoCompleteIntent() }
            }
            setOnClickListener { startAutoCompleteIntent() }
        }
    }

    private fun initPlacesApi() {
        // Define a variable to hold the Places API key.
        val apiKey = BuildConfig.PLACES_API_KEY

        // Log an error if apiKey is not set.
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("Places test", "No api key")
            finish()
            return
        }

        // Initialize the SDK
        Places.initialize(applicationContext, apiKey)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)
    }

    private val startAutocomplete = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val intent = result.data
            val status: Status = Autocomplete.getStatusFromIntent(result.data)
            Log.e(TAG, status.toString())
            if (intent != null) {
                val place = Autocomplete.getPlaceFromIntent(intent)
                binding.searchEditText.setText(place.name)

                // Write a method to read the address components from the Place
                // and populate the form with the address components
                Log.d(TAG, "Place: " + place.addressComponents)
                //fillInAddress(place)
            }
        } else if (result.resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
            Log.i(TAG, "User canceled autocomplete")
        }
        binding.searchEditText.clearFocus()
    }

    private fun startAutoCompleteIntent() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.LOCATION, Place.Field.DISPLAY_NAME)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(this)
        startAutocomplete.launch(intent)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
