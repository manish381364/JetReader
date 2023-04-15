package com.littlebit.jetreader.screens.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.littlebit.jetreader.components.JetReaderAppBar
import com.littlebit.jetreader.components.ListCard
import com.littlebit.jetreader.navigation.JetScreens
import com.littlebit.jetreader.screens.home.HomeScreenViewModel

@Composable
fun FavoriteScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val leadingIconClickable = remember {
        mutableStateOf(true)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            JetReaderAppBar(
                title = "Favorite Books", showProfile = false,
                leadingIconOnClick = {
                    leadingIconClickable.value = false
                    navController.navigate(JetScreens.HomeScreen.name) {
                        popUpTo(JetScreens.HomeScreen.name) {
                            inclusive = true
                        }
                    }
                },
                leadingIcon = Icons.Rounded.ArrowBack,
                leadingIconClickable = leadingIconClickable
            )
        },
    ) { it ->
        val favBooks = viewModel.data.value.data?.filter { it.isFavorite }
        LazyColumn(modifier = Modifier.padding(it)) {
            items(favBooks ?: emptyList()) { book ->
                ListCard(book) {
                    val isFavorite = true
                    navController.navigate(JetScreens.BookDetailsScreen.name + "/${book.bookId}/$isFavorite")
                }
            }
        }
    }
}