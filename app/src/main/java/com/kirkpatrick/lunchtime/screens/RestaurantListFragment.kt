package com.kirkpatrick.lunchtime.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kirkpatrick.lunchtime.R
import com.kirkpatrick.lunchtime.ui.theme.FavoriteHeartUnselected
import com.kirkpatrick.lunchtime.ui.theme.ListBackgroundColor
import com.kirkpatrick.lunchtime.ui.theme.RatingStarFilled
import com.kirkpatrick.lunchtime.ui.theme.RatingStarUnfilled
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {

    private val restaurantSearchViewModel by activityViewModels<RestaurantSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                val restaurants by restaurantSearchViewModel.restaurants.collectAsState()
                val loading by restaurantSearchViewModel.loading.collectAsState()

                MaterialTheme {
                    if(loading) {
                        Loader()
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = ListBackgroundColor)
                                .padding(horizontal = 16.dp)
                        ) {
                            itemsIndexed(
                                restaurants,
                                key = { _, restaurant -> restaurant.id }
                            ) { index, restaurant ->
                                if (index == 0) {
                                    HorizontalDivider(thickness = 16.dp, color = ListBackgroundColor)
                                }
                                RestaurantCardView(
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    restaurant = restaurant,
                                    onClick = { uiPlace -> navigateToDetailScreen(uiPlace) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToDetailScreen(uiPlace: UiPlace) {
        val directions = RestaurantListFragmentDirections
            .actionRestaurantListFragmentToRestaurantDetailFragment(uiPlace)
        findNavController().navigate(directions)
    }

}

@Composable
fun RestaurantCardView(
    modifier: Modifier = Modifier,
    restaurant: UiPlace,
    onClick: (UiPlace) -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .clickable { onClick(restaurant) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )

    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 18.dp)
                .fillMaxWidth()
                .height(72.dp)
        ) {
            Image(
                modifier = Modifier.size(width = 72.dp, height = 72.dp),
                painter = painterResource(id = R.drawable.im_trail_placeholder),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = restaurant.name,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                StarRatingView(
                    rating = restaurant.rating,
                    totalRatings = restaurant.userRatingCount
                )
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = "$".repeat(restaurant.priceLevel),
                    fontSize = 12.sp
                )
            }
            Image( //TODO load image from API
                modifier = Modifier
                    .size(width = 22.dp, height = 20.dp),
                imageVector = Icons.Outlined.FavoriteBorder,
                colorFilter = ColorFilter.tint(FavoriteHeartUnselected),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun StarRatingView(
    modifier: Modifier = Modifier,
    rating: Double,
    totalRatings: Int) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for(i in 1..5) {
            Image(
                modifier = Modifier.size(width = 16.dp, 15.dp),
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    if(i <= rating) RatingStarFilled else RatingStarUnfilled
                )
            )
        }
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "($totalRatings)",
            fontSize = 12.sp
        )
    }
}

@Composable
fun Loader() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_loading))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}

@Preview(widthDp = 300, heightDp = 100)
@Composable
fun RestaurantCardViewPreview() {
    RestaurantCardView(
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
        onClick = {}
    )
}

@Preview
@Composable
fun PreviewStarRatingView() {
    StarRatingView(rating = 3.2, totalRatings = 142)
}
