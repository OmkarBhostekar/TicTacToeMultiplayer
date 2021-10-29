package com.omkarcodes.tictactoe.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.omkarcodes.tictactoe.presentation.ui.game.GameScreen
import com.omkarcodes.tictactoe.presentation.ui.lobby.LobbyScreen
import com.omkarcodes.tictactoe.presentation.ui.welcome.WelcomeScreen

@ExperimentalFoundationApi
@Composable
fun TicTocToeNavigation() {
    val navController = rememberNavController()
    Scaffold(backgroundColor = Color.Transparent) {
        Box(modifier = Modifier.padding(top = 32.dp)) {
            NavHost(
                navController = navController,
                startDestination = Screen.WelcomeScreen.route
            ) {
                composable(Screen.WelcomeScreen.route) {
                    WelcomeScreen(navController = navController)
                }
                composable(Screen.LobbyScreen.route) {
                    LobbyScreen(navController = navController)
                }
                composable(
                    route = Screen.GameScreen.route + "/{type}",
                    arguments = listOf(navArgument("type") { type = NavType.StringType })
                ) { backStackEntry ->
                    GameScreen(
                        t = backStackEntry.arguments?.getString("type")
                    )
                }
            }
        }
    }

}


@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}