// app/src/main/java/com/example/cryptotracker/ui/NavGraph.kt
package com.example.cryptotracker.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.cryptotracker.ui.dashboard.DashboardScreen
import com.example.cryptotracker.ui.home.HomeScreen
import com.example.cryptotracker.ui.alertDetail.AlertDetailScreen
import com.example.cryptotracker.ui.makeAlert.MakeAlertScreen
import com.example.cryptotracker.ui.notifications.NotificationsScreen
import com.example.cryptotracker.ui.portfolio.AddAssetScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Dashboard : Screen("dashboard")
    object Notifications : Screen("notifications")
    object MakeAlert : Screen("make_alert")
    object AddAsset : Screen("add_asset")
    object AlertDetail : Screen("alert_detail/{alertId}") {
        fun createRoute(alertId: Long) = "alert_detail/$alertId"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Market overview (Home)

        composable(Screen.Home.route) {
            HomeScreen(
                onAlertsClick      = { navController.navigate(Screen.Notifications.route) },
                onPortfolioClick   = { navController.navigate(Screen.Dashboard.route) }
            )
        }
        // Portfolio screen
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onBack = { navController.popBackStack() },
                onAdd = { navController.navigate(Screen.AddAsset.route) }
            )
        }
        composable(Screen.AddAsset.route) {
            AddAssetScreen(
                onDone = { navController.popBackStack() }
            )
        }
        // Alerts list
        composable(Screen.Notifications.route) {
            NotificationsScreen(
                onAddClick = { navController.navigate(Screen.MakeAlert.route) },
                onAlertClick = { id -> navController.navigate(Screen.AlertDetail.createRoute(id)) },
                onDeleteClick = { /* handled in ViewModel */ }
            )
        }
        // Make a new alert
        composable(Screen.MakeAlert.route) {
            MakeAlertScreen(
                onDone = { navController.popBackStack() }
            )
        }
        // Alert detail / edit, via deep link or navigation
        composable(
            route = Screen.AlertDetail.route,
            arguments = listOf(navArgument("alertId") { type = NavType.LongType }),
            deepLinks = listOf(navDeepLink { uriPattern = "cryptotracker://alert/{alertId}" })
        ) {
            AlertDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}