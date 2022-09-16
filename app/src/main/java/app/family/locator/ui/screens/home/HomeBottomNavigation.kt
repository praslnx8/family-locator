package app.family.locator.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import app.family.locator.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomNavigation(
    chatCountFromHome: Int,
    navController: NavController
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Chat,
        BottomNavItem.Map,
        BottomNavItem.Profile
    )

    val chatCountState = remember(chatCountFromHome) {
        mutableStateOf(chatCountFromHome)
    }


    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    if (item == BottomNavItem.Chat && chatCountState.value > 0) {
                        BadgedBox(badge = { Text(text = chatCountState.value.toString()) }) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = stringResource(id = item.titleResId)
                            )
                        }

                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(id = item.titleResId)
                        )
                    }

                },
                label = { Text(text = stringResource(id = item.titleResId)) },
                selected = currentRoute == item.route,
                onClick = {
                    if (item == BottomNavItem.Chat) {
                        chatCountState.value = 0
                    }
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


sealed class BottomNavItem(
    val route: String,
    @StringRes val titleResId: Int,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = Screen.Home.route,
        titleResId = R.string.home,
        icon = Icons.Default.Home
    )

    object Chat : BottomNavItem(
        route = Screen.Chat.route,
        titleResId = R.string.chat,
        icon = Icons.Default.Email
    )

    object Map : BottomNavItem(
        route = Screen.Map.route,
        titleResId = R.string.map,
        icon = Icons.Default.Place
    )

    object Profile : BottomNavItem(
        route = Screen.Profile.route,
        titleResId = R.string.profile,
        icon = Icons.Default.AccountBox
    )
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Chat : Screen("chat")
    object Map : Screen("map")
    object Profile : Screen("profile")
}