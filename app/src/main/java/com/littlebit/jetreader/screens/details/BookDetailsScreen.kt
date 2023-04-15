package com.littlebit.jetreader.screens.details

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.littlebit.jetreader.components.AboutBook
import com.littlebit.jetreader.components.BookDetailsImage
import com.littlebit.jetreader.components.ExpandableCard
import com.littlebit.jetreader.components.JetReaderAppBar
import com.littlebit.jetreader.components.floatingActionOnClick
import com.littlebit.jetreader.screens.home.HomeScreenViewModel


@Composable
fun BookDetailsScreen(
    navController: NavHostController,
    bookId: String?,
    viewModel: BookDetailsViewModel = hiltViewModel(),
    isFavorite: Boolean = false
) {
    viewModel.getBookById(bookId ?: "")
    Log.d("BOOK_ID", "BookDetailsScreen: $bookId")
    val context = LocalContext.current
    val leadingIconClickable = remember {
        mutableStateOf(true)
    }
    Scaffold(
        topBar = {
            JetReaderAppBar(
                title = "Book Details",
                showProfile = false,
                leadingIcon = Icons.Rounded.ArrowBack,
                leadingIconOnClick = {
                    leadingIconClickable.value = false
                    navController.popBackStack()
                },
                leadingIconClickable = leadingIconClickable
            )
        },

        ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(7.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    val isLoading = remember {
                        mutableStateOf(true)
                    }
                    if (isLoading.value) {
                        LinearProgressIndicator(Modifier.align(Alignment.Center))
                    }
                    BookDetailsImage(viewModel, isLoading)

                }
                AboutBook(viewModel)
                if (viewModel.bookResource.value.data?.volumeInfo?.description.toString()
                        .isNotEmpty()
                )
                    ExpandableCard(viewModel)
                else
                    Text("No description available")
            }

            if (!isFavorite) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val homeViewModel: HomeScreenViewModel = hiltViewModel()
                    val saveClickable = remember {
                        mutableStateOf(true)
                    }
                    FloatingActionButton(onClick = {
                        floatingActionOnClick(
                            viewModel,
                            navController,
                            homeViewModel,
                            saveClickable,
                            context
                        )
                    }) {
                        Text("Save")
                    }
                    FloatingActionButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}



