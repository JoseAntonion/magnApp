package com.example.magnapp.ui.screens.main

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.magnapp.R

sealed class NavItem(
    var title: String,
    var icon: Int,
    var screenRoute: String,
    val navArgs: List<NavArgs> = emptyList()
) {
    val route = run {
        // baseroute/{arg1}/{arg2}
        val argKeys = navArgs.map { "{${it.key}}" }
        listOf(screenRoute)
            .plus(argKeys)
            .joinToString("/")
    }

    val args = navArgs.map {
        navArgument(it.key) { type = it.navType }
    }

    object OldOneNavItem : NavItem(
        title = "1980",
        icon = R.drawable.ic_search,
        screenRoute = "old_one"
    )

    object MainNavItem : NavItem(
        title = "Buscar",
        icon = R.drawable.ic_search,
        screenRoute = "buscar"
    )

    object NewOneNavItem : NavItem(
        title = "2022",
        icon = R.drawable.ic_search,
        screenRoute = "new_one"
    )

    object DetalleNavItem : NavItem(
        title = "detalle titulo",
        icon = R.drawable.ic_search,
        screenRoute = "detalle",
        navArgs = listOf(NavArgs.TopicoId)
    ) {
        fun createNavRoute(topicoId: String) = "$screenRoute/$topicoId"
    }
}

enum class NavArgs(val key: String, val navType: NavType<*>) {
    TopicoId("topicoId", NavType.StringType),
    TopicoName("topicoName", NavType.StringType),
}