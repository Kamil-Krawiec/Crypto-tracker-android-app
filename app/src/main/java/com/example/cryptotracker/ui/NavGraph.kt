// app/src/main/java/com/example/cryptotracker/NavGraph.kt
package com.example.cryptotracker.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.cryptotracker.data.repository.AlertRepository
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.ui.dashboard.DashboardScreen
import com.example.cryptotracker.ui.home.HomeScreen
import com.example.cryptotracker.ui.makealert.AlertDetailScreen
import com.example.cryptotracker.ui.makealert.MakeAlertScreen
import com.example.cryptotracker.ui.notifications.NotificationsScreen

sealed class Screen(val route: String) {
  object Home          : Screen("home")
  object Dashboard     : Screen("dashboard")
  object Notifications : Screen("notifications")
  object MakeAlert     : Screen("make_alert")
  // new detail screen, with {alertId} placeholder
  object AlertDetail   : Screen("alert_detail/{alertId}")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CryptoTrackerNavGraph(
  navController: NavHostController = rememberNavController(),
  cryptoRepo: CryptoRepository,
  alertRepo: AlertRepository
) {
  NavHost(
    navController      = navController,
    startDestination   = Screen.Home.route
  ) {
    composable(Screen.Home.route) {
      HomeScreen(
        cryptoRepo      = cryptoRepo,
        onPortfolioClick = { navController.navigate(Screen.Dashboard.route) }
      )
    }

    composable(Screen.Dashboard.route) {
      DashboardScreen(cryptoRepo = cryptoRepo)
    }

    composable(Screen.Notifications.route) {
      NotificationsScreen(
        alertRepo     = alertRepo,
        onAddClick    = { navController.navigate(Screen.MakeAlert.route) },
        onAlertClick  = { id ->
          navController.navigate("alert_detail/$id")
        }
      )
    }

    composable(Screen.MakeAlert.route) {
      MakeAlertScreen(
        alertRepo = alertRepo,
        onDone    = { navController.popBackStack(Screen.Notifications.route, false) }
      )
    }

    composable(
      route = Screen.AlertDetail.route,
      arguments = listOf(navArgument("alertId") { type = NavType.LongType }),
      deepLinks = listOf(
        navDeepLink { uriPattern = "cryptotracker://alert/{alertId}" }
      )
    ) { backStackEntry ->
      val alertId = backStackEntry.arguments!!.getLong("alertId")
      AlertDetailScreen(
        alertId   = alertId,
        alertRepo = alertRepo,
        onBack    = { navController.popBackStack() }
      )
    }
  }
}