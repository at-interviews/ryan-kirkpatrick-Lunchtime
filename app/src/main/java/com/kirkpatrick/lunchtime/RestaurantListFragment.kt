package com.kirkpatrick.lunchtime

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.places.api.model.Place
import com.kirkpatrick.lunchtime.databinding.FragmentRestaurantListBinding
import com.kirkpatrick.lunchtime.ui.theme.FavoriteHeartUnselected
import com.kirkpatrick.lunchtime.ui.theme.ListBackgroundColor
import com.kirkpatrick.lunchtime.ui.theme.RatingStarFilled
import com.kirkpatrick.lunchtime.ui.theme.RatingStarUnfilled
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random

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

                MaterialTheme {
                    LazyColumn( //TODO check on this
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = ListBackgroundColor)
                            .padding(horizontal = 16.dp)
                    ) {
                        itemsIndexed(
                            restaurants,
                            key = { _, restaurant -> restaurant.id }) { index, restaurant ->
                            if (index == 0) {
                                HorizontalDivider(thickness = 16.dp, color = ListBackgroundColor)
                            }
                            RestaurantCardView(
                                modifier = Modifier.padding(bottom = 16.dp),
                                restaurant = restaurant
                            )
                        }

                    }
                }
            }
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restaurantSearchViewModel.searchNearby()
        lifecycleScope.launch {
            restaurantSearchViewModel.restaurants.collect { restaurants ->

                
            }
        }
    }
}


//@Preview(widthDp = 300, heightDp = 100)
//@Composable
//fun RestaurantCardViewPreview() {
//    RestaurantCardView()
//}

@Composable
fun RestaurantCardView(
    modifier: Modifier = Modifier,
    restaurant: Place
) {
    OutlinedCard(
        modifier = modifier,
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
                contentDescription = "Restaurant Image",
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = restaurant.displayName ?: "",
                    fontSize = 16.sp,
                    maxLines = 1
                )
                StarRatingView(
                    rating = restaurant.rating ?: 0.0,
                    totalRatings = restaurant.userRatingCount ?: 0
                )
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = "$".repeat(restaurant.priceLevel ?: 1),
                    fontSize = 12.sp
                )
            }
            Image(
                modifier = Modifier
                    .size(width = 22.dp, height = 20.dp),
                imageVector = Icons.Outlined.FavoriteBorder,
                colorFilter = ColorFilter.tint(FavoriteHeartUnselected),
                contentDescription = "Favorite Button"
            )
        }
    }
}

@Preview
@Composable
fun PreviewStarRatingView() {
    StarRatingView(rating = 3.2, totalRatings = 142)
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

