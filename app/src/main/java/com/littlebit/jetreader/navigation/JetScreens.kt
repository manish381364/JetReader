package com.littlebit.jetreader.navigation

enum class JetScreens {
    SplashScreen,
    SearchScreen,
    LoginSignUpScreen,
    CreateAccountScreen,
    HomeScreen,
    BookDetailsScreen,
    StatsScreen,
    UpdateScreen;

    companion object {
        fun fromRoute(route: String): JetScreens
         = when(route?.substringBefore("/")){
            SplashScreen.name -> SplashScreen
            SearchScreen.name -> SearchScreen
            LoginSignUpScreen.name -> LoginSignUpScreen
            CreateAccountScreen.name -> CreateAccountScreen
            HomeScreen.name -> HomeScreen
            BookDetailsScreen.name -> BookDetailsScreen
            StatsScreen.name -> StatsScreen
            UpdateScreen.name -> UpdateScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Unknown route: $route")
         }
    }
}