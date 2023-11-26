package com.cencen.luminaluxe

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cencen.luminaluxe.ui.navigation.ItemNavigation
import com.cencen.luminaluxe.ui.navigation.Screen
import com.cencen.luminaluxe.ui.screen.about.AboutScreen
import com.cencen.luminaluxe.ui.screen.cart.CartScreen
import com.cencen.luminaluxe.ui.screen.details.DetailsScreen
import com.cencen.luminaluxe.ui.screen.main.MainScreen
import com.cencen.luminaluxe.ui.theme.LuminaLuxeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuminaLuxeApp(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
) {
    val navBackStack by navHostController.currentBackStackEntryAsState()
    val currentRoutes = navBackStack?.destination?.route

    Scaffold (
        bottomBar = {
            if (currentRoutes != Screen.DetailSkincare.routes) {
                BottomBar(navHostController) }
            },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = Screen.Main.routes,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Main.routes) {
                MainScreen(
                    navToDetail = { skincareId ->
                        navHostController.navigate(Screen.DetailSkincare.createRoutes(skincareId))
                    }
                )
            }
            composable(Screen.Cart.routes) {
                val context = LocalContext.current
                CartScreen(
                    onOrderBtnClicked = { mess ->
                    shareOrders(context, mess)
                })
            }
            composable(Screen.About.routes) {
                AboutScreen()
            }
            composable(
                route = Screen.DetailSkincare.routes,
                arguments = listOf(navArgument("skincareId") { type = NavType.LongType}),
            ) {
                val skincaresId = it.arguments?.getLong("skincareId") ?: -1L
                DetailsScreen(
                    skincareId = skincaresId,
                    navToBack = { navHostController.navigateUp() },
                    navToCart = {
                        navHostController.popBackStack()
                        navHostController.navigate(Screen.Cart.routes) {
                            popUpTo(navHostController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

}


private fun shareOrders(ctx: Context, sum: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, ctx.getString(R.string.luminaluxe))
        putExtra(Intent.EXTRA_TEXT, sum)
    }

    ctx.startActivity(
        Intent.createChooser(
            intent, ctx.getString(R.string.luminaluxe)
        )
    )
}

@Composable
fun BottomBar(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar (
        modifier = modifier,
    ) {
        val navBackStack by navHostController.currentBackStackEntryAsState()
        val currentRoutes = navBackStack?.destination?.route
        val navItems = listOf(
            ItemNavigation(
                title = stringResource(R.string.main),
                icons = R.drawable.main_icons,
                screen = Screen.Main
            ),
            ItemNavigation(
                title = stringResource(R.string.cart),
                icons = R.drawable.cart_icons,
                screen = Screen.Cart
            ),
            ItemNavigation(
                title = stringResource(R.string.about),
                icons = R.drawable.about_icons,
                screen = Screen.About
            ),
        )
        navItems.map { items ->
            NavigationBarItem(
                selected = currentRoutes == items.screen.routes,
                onClick = {
                    navHostController.navigate(items.screen.routes) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    } },
                icon = {
                    Icon(
                        painter = painterResource(id = items.icons),
                        contentDescription = items.title)
                })
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun LuminaLuxeAppPreview() {
    LuminaLuxeTheme {
        LuminaLuxeApp()
    }
}