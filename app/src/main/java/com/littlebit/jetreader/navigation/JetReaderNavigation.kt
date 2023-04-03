package com.littlebit.jetreader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.littlebit.jetreader.screens.JetReaderSplashScreen
import com.littlebit.jetreader.screens.createAccountScreen.CreateAccountScreen
import com.littlebit.jetreader.screens.details.BookDetailsScreen
import com.littlebit.jetreader.screens.details.BookDetailsViewModel
import com.littlebit.jetreader.screens.home.HomeScreen
import com.littlebit.jetreader.screens.login.LoginSignUpScreen
import com.littlebit.jetreader.screens.search.BooksSearchViewModel
import com.littlebit.jetreader.screens.search.SearchScreen
import com.littlebit.jetreader.screens.stats.StatsScreen
import com.littlebit.jetreader.screens.update.UpdateScreen
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun JetReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = JetScreens.SplashScreen.name) {
        composable(JetScreens.SplashScreen.name) {
            JetReaderSplashScreen(navController)
        }
        composable(JetScreens.HomeScreen.name) {
            HomeScreen(navController)
        }
        composable(JetScreens.SearchScreen.name) {
            val viewModel: BooksSearchViewModel = hiltViewModel()
            SearchScreen(navController, viewModel)
        }
        composable(JetScreens.LoginSignUpScreen.name) {
            LoginSignUpScreen(navController)
        }
        composable(JetScreens.CreateAccountScreen.name) {
            CreateAccountScreen(navController)
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
        composable(JetScreens.StatsScreen.name) {
            StatsScreen(navController)
        }
        composable(JetScreens.UpdateScreen.name) {
            UpdateScreen(navController)
        }

    }
}