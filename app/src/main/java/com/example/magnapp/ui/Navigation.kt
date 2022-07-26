package com.example.magnapp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.magnapp.ui.screens.detalle_topico.DetalleTopicoScreen
import com.example.magnapp.ui.screens.main.MainScreen
import com.example.magnapp.ui.screens.main.NavArgs
import com.example.magnapp.ui.screens.main.NavItem
import com.example.magnapp.ui.screens.new.NewOneScreen
import com.example.magnapp.ui.screens.old.OldOneScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
//    val mainViewModel = MainActivity().viewModel
//    val detalleViewModel = viewModel<DetalleTopicoViewModel>()

    NavHost(
        navController = navController,
        startDestination = NavItem.MainNavItem.route
    ) {
        composable(NavItem.MainNavItem) {
            MainScreen(
                navController = navController,
                viewModel = viewModel()
            ) {
                val route = NavItem.DetalleNavItem.createNavRoute(it)
                navController.navigate(route)
            }
        }
        composable(NavItem.OldOneNavItem) {
            OldOneScreen()
        }
        composable(NavItem.NewOneNavItem) {
            NewOneScreen()
        }
        composable(NavItem.DetalleNavItem) { backStackEntry ->
            val topicoId: String = backStackEntry.findArg(NavArgs.TopicoId)
            DetalleTopicoScreen(
                viewModel = viewModel(),
                topicoId = topicoId
            ) {
                navController.popBackStack()
            }
        }
    }
}

private inline fun <reified T> NavBackStackEntry.findArg(arg: NavArgs): T {
    val value = arguments?.get(arg.key)
    requireNotNull(value)
    return value as T
}

private fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = navItem.route,
        arguments = navItem.args
    ) {
        content(it)
    }
}