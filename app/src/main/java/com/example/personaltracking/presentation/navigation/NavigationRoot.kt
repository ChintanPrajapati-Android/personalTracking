package com.example.personaltracking.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.personaltracking.presentation.list.LocationListScreen
import com.example.personaltracking.presentation.map.MapScreen

@Composable
fun NavigationRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MAP
    ) {
        composable(Routes.MAP) {
            MapScreen(
                onAction = { action ->
                    when (action) {
                        MapScreenActions.OpenListScreen -> {
                            navController.navigate(Routes.LIST)
                        }
                    }
                }
            )
        }

        composable(Routes.LIST) {
            LocationListScreen(
                onAction = { action ->
                    when (action) {
                        LocationListActions.Back -> {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}

object Routes {
    const val MAP = "map"
    const val LIST = "list"
}
