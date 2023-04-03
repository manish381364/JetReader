package com.littlebit.jetreader.screens.update

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.littlebit.jetreader.R
import com.littlebit.jetreader.screens.home.BookRating
import com.littlebit.jetreader.screens.home.JetReaderAppBar
import com.littlebit.jetreader.screens.home.RoundedButton
import kotlinx.coroutines.delay
import java.lang.Math.ceil
import java.lang.Math.floor

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(navController: NavHostController = rememberNavController()) {
    val scrollState = rememberLazyListState()
    val appBarVisible = remember { mutableStateOf(true) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            JetReaderAppBar(
                title = "Update Book",
                navController = navController,
                showProfile = false,
                leadingIcon = Icons.TwoTone.ArrowBack
            )
            ElevatedCard(
                modifier = Modifier.size(200.dp),
                shape = RoundedCornerShape(30.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp)
                ) {
                    AsyncImage(model = "", contentDescription = null)
                    Column() {
                        Text(text = "Title", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Author", style = MaterialTheme.typography.bodySmall)
                        Text(text = "Date", style = MaterialTheme.typography.bodySmall)

                    }
                }
            }

            Divider(
                Modifier
                    .fillMaxWidth(0.5f)
                    .padding(20.dp)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(12.dp)
                    .requiredHeightIn(min = 25.dp),
                value = "", onValueChange = {}
            )

            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("")
                Text("")
            }
            Spacer(Modifier.height(10.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text("Start Reading")
                Text("Mark as Read")
            }
            Spacer(modifier = Modifier.height(25.dp))
            RatingBar(rating = 2f, maxRating = 5) {

            }
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                RoundedButton(label = "Update", modifier = Modifier.padding(8.dp))
                RoundedButton(label = "Delete", modifier = Modifier.padding(8.dp))
            }
        }
    }

}


@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    maxRating: Int,
    onRatingChanged: (Float) -> Unit
) {
    Row {
        for (i in 1..maxRating) {
            val drawableRes = if (i <= rating) {
                Icons.Filled.Star
            } else {
                Icons.Outlined.StarRate
            }
            Icon(
                imageVector = drawableRes,
                contentDescription = null,
                modifier = modifier
                    .size(40.dp)
                    .clickable { onRatingChanged(i.toFloat()) },
                tint = Color.Yellow
            )
        }
    }
}







