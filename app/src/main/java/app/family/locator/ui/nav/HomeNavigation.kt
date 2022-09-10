package app.family.locator.ui.nav

import PermissionCheckScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import app.family.locator.ui.route.ScreenRoute
import app.family.locator.ui.screens.HomeScreen
import app.family.locator.ui.screens.LoginScreen
import app.family.locator.ui.screens.UserDetailScreen

@Composable
fun HomeNavigation(
    requestBackgroundPermission: () -> Unit
) {
    val navController = rememberNavController()
    val deepLinkUri = "family://"
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Login.TEMPLATE,
    ) {
        composable(
            route = ScreenRoute.Login.TEMPLATE,
        ) {
            LoginScreen(onLogin = {
                navController.navigate(ScreenRoute.PermissionCheck.TEMPLATE)
            })
        }
        composable(
            route = ScreenRoute.PermissionCheck.TEMPLATE,
        ) {
            PermissionCheckScreen(
                onPermissionsGranted = {
                    navController.navigate(ScreenRoute.Home.TEMPLATE)
                },
                requestBackgroundPermission = {
                    requestBackgroundPermission()
                })
        }
        composable(
            route = ScreenRoute.Home.TEMPLATE,
            deepLinks = listOf(navDeepLink { uriPattern = deepLinkUri + ScreenRoute.Home.TEMPLATE })
        ) {
            HomeScreen()
        }
        composable(
            route = ScreenRoute.UserDetail.TEMPLATE,
            deepLinks = listOf(navDeepLink {
                uriPattern = deepLinkUri + ScreenRoute.UserDetail.TEMPLATE
            }),
            arguments = listOf(
                navArgument(ScreenRoute.UserDetail.USER_ID_ARG) {
                    type = NavType.LongType
                }
            )
        ) { navBackStackEntry ->
            UserDetailScreen(
                userId = navBackStackEntry.arguments?.getString(ScreenRoute.UserDetail.USER_ID_ARG)!!
            )
        }
    }
}
