package app.family.locator.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
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