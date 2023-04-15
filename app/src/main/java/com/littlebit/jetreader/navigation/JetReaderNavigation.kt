package com.littlebit.jetreader.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.littlebit.jetreader.screens.JetReaderSplashScreen
import com.littlebit.jetreader.screens.details.BookDetailsScreen
import com.littlebit.jetreader.screens.details.BookDetailsViewModel
import com.littlebit.jetreader.screens.drawer.AppDrawer
import com.littlebit.jetreader.screens.favorite.FavoriteScreen
import com.littlebit.jetreader.screens.home.HomeScreen
import com.littlebit.jetreader.screens.home.HomeScreenViewModel
import com.littlebit.jetreader.screens.login.LoginSignUpScreen
import com.littlebit.jetreader.screens.search.BooksSearchViewModel
import com.littlebit.jetreader.screens.search.SearchScreen
import com.littlebit.jetreader.screens.stats.StatsScreen
import com.littlebit.jetreader.screens.update.UpdateScreen


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun JetReaderNavigation(isDarkTheme: MutableState<Boolean>) {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController, startDestination = JetScreens.SplashScreen.name) {
        composable(
            JetScreens.SplashScreen.name,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(800))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(800))
            }

        ) {
            JetReaderSplashScreen(navController)
        }
        composable(
            JetScreens.HomeScreen.name,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            }
        ) {
            val viewModel: HomeScreenViewModel = hiltViewModel()
            HomeScreen(navController, viewModel = viewModel)
        }
        composable(
            JetScreens.SearchScreen.name,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(800))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            },
        ) {
            val viewModel: BooksSearchViewModel = hiltViewModel()
            SearchScreen(navController, viewModel)
        }
        composable(
            JetScreens.LoginSignUpScreen.name,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(800))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(800))
            }
        ) {
            LoginSignUpScreen(navController)
        }
        composable(
            JetScreens.BookDetailsScreen.name
                    + "/{bookId}/{isFavorite}",
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.StringType
                },
                navArgument("isFavorite") {
                    type = NavType.BoolType
                }
            ),
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(800))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(800))
            }
        ) {
            val viewModel: BookDetailsViewModel = hiltViewModel()
            val bookId = it.arguments?.getString("bookId")
            val isFavorite = it.arguments?.getBoolean("isFavorite")
            BookDetailsScreen(navController, bookId, viewModel, isFavorite ?: false)
        }
        composable(
            JetScreens.UpdateScreen.name + "/{bookId}",
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(800))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(800))
            }
        ) {
            val viewModel: HomeScreenViewModel = hiltViewModel()
            val bookId = it.arguments?.getString("bookId")
            UpdateScreen(navController, bookId, viewModel)
        }

        composable(
            JetScreens.StatsScreen.name,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(800))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(800))
            }
        ) {
            val viewModel: HomeScreenViewModel = hiltViewModel()
            StatsScreen(navController, viewModel)
        }
        composable(
            JetScreens.FavoriteScreen.name,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            }
        ) {
            val viewModel: HomeScreenViewModel = hiltViewModel()
            FavoriteScreen(navController, viewModel)
        }
        composable(
            JetScreens.DrawerScreen.name,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(700))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(700))
            }
        ) {
            AppDrawer(navController, isDarkTheme)
        }
    }
}

