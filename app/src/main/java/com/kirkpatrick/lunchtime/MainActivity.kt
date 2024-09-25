package com.kirkpatrick.lunchtime

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kirkpatrick.lunchtime.databinding.MainActivityLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityLayoutBinding
    private val restaurantViewModel by viewModels<RestaurantSearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun closeKeyboard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
