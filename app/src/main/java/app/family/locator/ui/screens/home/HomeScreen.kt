package app.family.locator.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            HomeBottomNavigation(navController)
        },
    ) {
        HomeNavigationView(navController = navController, modifier = Modifier.padding(it))
    }
}