package com.kirkpatrick.lunchtime.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kirkpatrick.lunchtime.R

class RestaurantDetailFragment : Fragment() {

    val args: RestaurantDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    RestaurantDetailScreen(
                        restaurant = args.restaurant,
                        onBackClick = { findNavController().popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
fun RestaurantDetailScreen(
    modifier: Modifier = Modifier,
    restaurant: UiPlace,
    onBackClick: () -> Unit) {
    Column(modifier = modifier) {
        Image(
            modifier = Modifier
                .clickable { onBackClick() }
                .padding(top = 2.dp, start = 2.dp, end = 2.dp, bottom = 4.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = ""
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            painter = painterResource(id = R.drawable.im_trail_placeholder),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = restaurant.name,
                fontSize = 20.sp,
                maxLines = 1
            )
            Text(
                modifier = Modifier.padding(start = 2.dp, top = 4.dp),
                text = restaurant.address,
                fontSize = 12.sp
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                StarRatingView(
                    modifier = Modifier.padding(top = 2.dp),
                    rating = restaurant.rating,
                    totalRatings = restaurant.userRatingCount
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                    text = "$".repeat(restaurant.priceLevel),
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier.padding(start = 2.dp, top = 16.dp),
                text = restaurant.description,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(widthDp = 350, heightDp = 450, showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun RestaurantDetailFragmentPreview() {
    RestaurantDetailScreen(
        restaurant = UiPlace(
            id = "123",
            rating = 4.2,
            userRatingCount = 460,
            priceLevel = 2,
            name = "Roberto's",
            latitude = 32.7157,
            longitude = -117.161,
            address = "123 Fake St, San Diego, CA",
            description = "This is a description of the restaurant"
        ),
        onBackClick = {}
    )
}