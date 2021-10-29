package com.omkarcodes.tictactoe.presentation.navigation

sealed class Screen(val route: String) {
    object WelcomeScreen : Screen("welcome")
    object HomeScreen : Screen("home")
}
