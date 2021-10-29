package com.omkarcodes.tictactoe.presentation.navigation

sealed class Screen(val route: String) {
    object WelcomeScreen : Screen("welcome")
    object GameScreen : Screen("game")
    object LobbyScreen : Screen("lobby")
}
