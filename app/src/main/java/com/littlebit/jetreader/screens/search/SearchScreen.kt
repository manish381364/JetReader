package com.littlebit.jetreader.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val leadingIconClickable = remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            JetReaderAppBar(
                title = "Search",
                leadingIcon = Icons.Default.ArrowBack,
                leadingIconOnClick = {
                    leadingIconClickable.value = false
                    navController.popBackStack()
                },
                showProfile = false,
                leadingIconClickable = leadingIconClickable,
            )
        },

        ) {
        SearchScreenFullContent(it, navController, viewModel)
    }
}







