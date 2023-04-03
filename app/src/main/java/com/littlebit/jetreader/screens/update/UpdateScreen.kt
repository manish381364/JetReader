package com.littlebit.jetreader.screens.update

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.littlebit.jetreader.screens.details.BookDetailsViewModel
import com.littlebit.jetreader.screens.home.JetReaderAppBar
import com.littlebit.jetreader.screens.home.RoundedButton


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun UpdateScreen(
    navController : NavController = rememberNavController(),
    bookId: String?,
    viewModel: BookDetailsViewModel = hiltViewModel()
) {
    viewModel.getBookById(bookId?: "")
    Scaffold(
        topBar = {
            JetReaderAppBar(
                title = "Update Book",
                navController = navController,
                showProfile = false,
                leadingIcon = Icons.TwoTone.ArrowBack,
                leadingIconOnClick = { navController.popBackStack() }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if(viewModel.bookResource.value.data == null){
                LinearProgressIndicator(Modifier.align(Alignment.Center))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(200.dp),
                    shape = RoundedCornerShape(80.dp)
                ) {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        LoadImage(viewModel)
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly,
                            ) {
                                Text(text = viewModel.bookResource.value.data?.volumeInfo?.title.toString(), style = MaterialTheme.typography.titleMedium,
                                    fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                                )
                                Text(text = viewModel.bookResource.value.data?.volumeInfo?.authors.toString().removePrefix("[").removeSuffix("]"), style = MaterialTheme.typography.bodySmall)
                                Text(text = viewModel.bookResource.value.data?.volumeInfo?.publishedDate.toString(), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                Divider(
                    Modifier
                        .fillMaxWidth(0.8f)
                        .padding(20.dp)
                )
                var inputText by rememberSaveable { mutableStateOf("") }
                val keyBoardController = LocalSoftwareKeyboardController.current
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(12.dp)
                        .requiredHeightIn(min = 25.dp),
                    value = inputText, onValueChange = { updatedValue ->
                        inputText = updatedValue
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyBoardController?.hide()
                        }
                    ),
                    label = { Text(text = "Review") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val isStartedReading = remember{
                        mutableStateOf(false)
                    }
                    TextButton(onClick = {}){
                    }
                    TextButton(onClick = {}){
                        Text(text = "2")
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                RatingBar(rating = 2f, maxRating = 5) {

                }
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    RoundedButton(
                        label = "Update",
                        modifier = Modifier.padding(8.dp),
                        isClickable = true
                    ) {

                    }
                    RoundedButton(
                        label = "Delete",
                        modifier = Modifier.padding(8.dp),
                        isClickable = true
                    ) {

                    }
                }
            }
        }
    }

}

@Composable
fun LoadImage(
    viewModel: BookDetailsViewModel,
) {
    AsyncImage(
        model = viewModel.bookResource.value.data?.volumeInfo?.imageLinks?.thumbnail,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .size(100.dp),
    )
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







