package app.family.locator.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import app.family.presentation.vms.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    val navController = rememberNavController()
    val count = viewModel.getUnReadMessageCount().collectAsState(initial = 0)

    Scaffold(
        bottomBar = {
            HomeBottomNavigation(count.value, navController)
        },
    ) {
        HomeNavigationView(navController = navController, modifier = Modifier.padding(it))
    }
}