package app.family.locator.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.family.locator.ui.screens.chat.ChatScreen
import app.family.locator.ui.screens.profile.ProfileScreen
import app.family.locator.ui.screens.status.StatusListScreen

@Composable
fun HomeNavigationView(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) {
            StatusListScreen()
        }
        composable(BottomNavItem.Chat.route) {
            ChatScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}