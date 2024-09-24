package com.kirkpatrick.lunchtime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kirkpatrick.lunchtime.databinding.FragmentRestaurantListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {

    private val restaurantSearchViewModel: RestaurantSearchViewModel by viewModels()

    private lateinit var binding: FragmentRestaurantListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = "Hip hip hooray list fragment"
        restaurantSearchViewModel.searchNearby()
    }
}