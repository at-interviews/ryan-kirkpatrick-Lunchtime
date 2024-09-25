package com.kirkpatrick.lunchtime

import android.os.Bundle
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


    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
