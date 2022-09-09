package app.family.locator.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import app.family.locator.ui.screens.HomeScreen
import app.family.locator.ui.screens.LoginScreen
import app.family.locator.ui.screens.UserDetailScreen
import app.family.locator.ui.route.ScreenRoute

@Composable
fun HomeNavigation() {
    val navController = rememberNavController()
    val deepLinkUri = "family://"
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Login.TEMPLATE,
    ) {
        composable(
            route = ScreenRoute.Login.TEMPLATE,
            deepLinks = listOf(navDeepLink {
                uriPattern = deepLinkUri + ScreenRoute.Login.TEMPLATE
            })
        ) {
            LoginScreen(onLogin = {
                navController.navigate(ScreenRoute.Home.TEMPLATE)
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
