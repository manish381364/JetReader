package com.littlebit.jetreader.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.littlebit.jetreader.components.JetReaderAppBar
import com.littlebit.jetreader.components.SearchScreenFullContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: BooksSearchViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            JetReaderAppBar(
                title = "Search",
                leadingIcon = Icons.Default.ArrowBack,
                leadingIconOnClick = { navController.popBackStack() },
                showProfile = false
            )
        },

        ) {
        SearchScreenFullContent(it, navController, viewModel)
    }
}







