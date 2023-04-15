package com.littlebit.jetreader.screens.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.littlebit.jetreader.components.JetReaderAppBar
import com.littlebit.jetreader.components.StatsScreenContent
import com.littlebit.jetreader.model.JetBook
import com.littlebit.jetreader.screens.home.HomeScreenViewModel
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel
) {
    var books: List<JetBook>
    val currentUser = FirebaseAuth.getInstance().currentUser
    val leadingIconClickable = remember {
        mutableStateOf(true)
    }
    if (!viewModel.data.value.loading!!)
        Scaffold(
            topBar = {
                JetReaderAppBar(
                    title = "Books Stats",
                    leadingIcon = Icons.Rounded.ArrowBack,
                    leadingIconOnClick = {
                        leadingIconClickable.value = false
                        navController.popBackStack()
                    },
                    showProfile = false,
                    leadingIconClickable = leadingIconClickable
                )
            }
        ) {
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { user -> user.userId == currentUser?.uid }
            } else {
                emptyList()
            }
            StatsScreenContent(it, currentUser, viewModel, books)

        }
}




