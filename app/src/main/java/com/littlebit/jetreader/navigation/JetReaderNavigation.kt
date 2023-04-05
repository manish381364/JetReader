package com.littlebit.jetreader.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.littlebit.jetreader.screens.JetReaderSplashScreen
import com.littlebit.jetreader.screens.details.BookDetailsScreen
import com.littlebit.jetreader.screens.details.BookDetailsViewModel
import com.littlebit.jetreader.screens.home.HomeScreen
import com.littlebit.jetreader.screens.home.HomeScreenViewModel
import com.littlebit.jetreader.screens.login.LoginSignUpScreen
import com.littlebit.jetreader.screens.search.BooksSearchViewModel
import com.littlebit.jetreader.screens.search.SearchScreen
import com.littlebit.jetreader.screens.stats.StatsScreen
import com.littlebit.jetreader.screens.update.UpdateScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = JetScreens.SplashScreen.name) {
        composable(JetScreens.SplashScreen.name) {
            JetReaderSplashScreen(navController)
        }
        composable(JetScreens.HomeScreen.name) {
            val viewModel: HomeScreenViewModel = hiltViewModel()
            HomeScreen(navController, viewModel = viewModel)
        }
        composable(JetScreens.SearchScreen.name) {
            val viewModel: BooksSearchViewModel = hiltViewModel()
            SearchScreen(navController, viewModel)
        }
        composable(JetScreens.LoginSignUpScreen.name) {
            LoginSignUpScreen(navController)
        }
        composable(
            JetScreens.BookDetailsScreen.name
                    + "/{bookId}",
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.StringType
                }
            )
        ) {
            val viewModel: BookDetailsViewModel = hiltViewModel()
            val bookId = it.arguments?.getString("bookId")
            BookDetailsScreen(navController, bookId, viewModel)
        }
        composable(JetScreens.UpdateScreen.name+"/{bookId}",
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.StringType
                }
            )
        ) {
            val viewModel: HomeScreenViewModel = hiltViewModel()
            val bookId = it.arguments?.getString("bookId")
            UpdateScreen(navController, bookId, viewModel)
        }

        composable(JetScreens.StatsScreen.name){
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            StatsScreen(navController, viewModel)
        }

    }
}