package app.family.locator.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.family.locator.ui.screens.chat.ChatScreen
import app.family.locator.ui.screens.map.MapScreen
import app.family.locator.ui.screens.profile.ProfileScreen
import app.family.locator.ui.screens.status.StatusListScreen

@Composable
fun HomeNavigationView(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = BottomNavItem.Home.route, modifier = modifier) {
        composable(BottomNavItem.Home.route) {
            StatusListScreen()
        }
        composable(BottomNavItem.Chat.route) {
            ChatScreen()
        }
        composable(BottomNavItem.Map.route) {
            MapScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}