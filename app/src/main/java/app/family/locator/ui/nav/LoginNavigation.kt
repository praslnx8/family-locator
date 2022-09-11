package app.family.locator.ui.nav

import PermissionCheckScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.family.locator.ui.route.ScreenRoute
import app.family.locator.ui.screens.LoginScreen

@Composable
fun LoginNavigation(onLoginCompleted: () -> Unit) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Login.TEMPLATE,
    ) {
        composable(
            route = ScreenRoute.Login.TEMPLATE,
        ) {
            LoginScreen(onLogin = {
                navController.navigate(ScreenRoute.PermissionCheck.TEMPLATE) {
                    popUpTo(0)
                }
            })
        }
        composable(
            route = ScreenRoute.PermissionCheck.TEMPLATE,
        ) {
            PermissionCheckScreen(
                onPermissionsGranted = {
                    onLoginCompleted()
                })
        }
    }
}
