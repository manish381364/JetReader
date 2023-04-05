package com.littlebit.jetreader.screens.details

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.littlebit.jetreader.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    navController: NavHostController,
    bookId: String?,
    viewModel: BookDetailsViewModel
) {
    // Sticky Top Image of book cover then scrollable content
    viewModel.getBookById(bookId ?: "")
    Scaffold(
        topBar = {
            JetReaderAppBar(
                title = "Book Details",
                showProfile = false,
                leadingIcon = Icons.Rounded.ArrowBack,
                leadingIconOnClick = { navController.popBackStack() }
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
                ExpandableCard(viewModel)
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FloatingActionButton(onClick = {
                    floatingActionOnClick(viewModel, navController)
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



