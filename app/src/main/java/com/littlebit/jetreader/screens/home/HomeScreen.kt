package com.littlebit.jetreader.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.littlebit.jetreader.components.FabContent
import com.littlebit.jetreader.components.HomeFullContent
import com.littlebit.jetreader.components.JetReaderAppBar
import com.littlebit.jetreader.navigation.JetScreens


@Composable
fun HomeScreen(
    navController: NavController = NavHostController(LocalContext.current),
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            JetReaderAppBar(
                title = "JetReader", showProfile = true,
                leadingIcon = Icons.Rounded.Menu,
                leadingIconOnClick = {
                    navController.navigate(JetScreens.DrawerScreen.name)
                },
                trailingIcon = Icons.Filled.Favorite,
            ) {
                navController.navigate(JetScreens.FavoriteScreen.name)
            }
        },
        floatingActionButton = {
            FabContent(onClick = { navController.navigate(JetScreens.SearchScreen.name) })
        },
    ) { padding ->
        HomeFullContent(padding, viewModel, navController)
    }
}






















