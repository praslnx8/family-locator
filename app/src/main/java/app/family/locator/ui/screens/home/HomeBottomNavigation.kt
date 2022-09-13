package app.family.locator.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import app.family.locator.R


@Composable
fun HomeBottomNavigation(
    navController: NavController
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Chat,
        BottomNavItem.Profile
    )

    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.titleResId)
                    )
                },
                label = { Text(text = stringResource(id = item.titleResId)) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
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

    object Profile : BottomNavItem(
        route = Screen.Profile.route,
        titleResId = R.string.profile,
        icon = Icons.Default.AccountBox
    )
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
}