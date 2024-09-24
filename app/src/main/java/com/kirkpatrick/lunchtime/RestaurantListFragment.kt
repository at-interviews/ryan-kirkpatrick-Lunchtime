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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.fragment.app.viewModels
import com.kirkpatrick.lunchtime.databinding.FragmentRestaurantListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {

    private val restaurantSearchViewModel: RestaurantSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color(0xF5F5F5FF))
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp)
                    ) {
                        for(i in 1..15) {
                            RestaurantCardView(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restaurantSearchViewModel.searchNearby()
    }
}


@Preview(widthDp = 300, heightDp = 100)
@Composable
fun RestaurantCardView(modifier: Modifier = Modifier) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
                    text = "Restaurant Name",
                    fontSize = 16.sp
                )
                StarRatingView(
                    rating = SystemClock.uptimeMillis() % 5.0,
                    totalRatings = 142
                )
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = "$$$",
                    fontSize = 12.sp
                )
            }
            Image(
                modifier = Modifier
                    .size(width = 22.dp, height = 20.dp),
                imageVector = Icons.Outlined.FavoriteBorder,
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
                    Color(if(i <= rating) 0xF5D24B00 else 0xE6E6E6FF)
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

