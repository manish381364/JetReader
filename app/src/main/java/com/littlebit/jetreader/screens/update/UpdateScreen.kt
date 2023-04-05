package com.littlebit.jetreader.screens.update

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.littlebit.jetreader.components.BookInfoCard
import com.littlebit.jetreader.components.Form
import com.littlebit.jetreader.components.JetReaderAppBar
import com.littlebit.jetreader.screens.home.HomeScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterial3Api
@Composable
fun UpdateScreen(
    navController: NavController = rememberNavController(),
    bookId: String?,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            JetReaderAppBar(
                title = "Update Book",
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
            val bookInfo = viewModel.data.value.data?.find { book -> book.id == bookId }
            if (bookInfo == null) {
                LinearProgressIndicator(Modifier.align(Alignment.Center))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                BookInfoCard(bookInfo)
                Divider(
                    Modifier
                        .fillMaxWidth(0.8f)
                        .padding(20.dp)
                )
                if (bookInfo != null)
                    Form(bookInfo, navController)
                else LinearProgressIndicator()
            }
        }
    }

}




















