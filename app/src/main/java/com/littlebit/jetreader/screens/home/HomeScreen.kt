package com.littlebit.jetreader.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.littlebit.jetreader.components.FabContent
import com.littlebit.jetreader.components.HomeFullContent
import com.littlebit.jetreader.components.JetReaderAppBar
import com.littlebit.jetreader.navigation.JetScreens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController = NavHostController(LocalContext.current),
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            JetReaderAppBar(title = "JetReader", showProfile = true,
                leadingIconOnClick = {
                    navController.navigate(JetScreens.FavoriteScreen.name)
                },
            ){
                navController.navigate(JetScreens.LoginSignUpScreen.name)
                navController.popBackStack(JetScreens.HomeScreen.name, inclusive = true, saveState = false)
                FirebaseAuth.getInstance().signOut()
            }
        },
        floatingActionButton = {
            FabContent(onClick = { navController.navigate(JetScreens.SearchScreen.name) })
        },
    ) { padding ->
        HomeFullContent(padding, viewModel, navController)
    }
}






















