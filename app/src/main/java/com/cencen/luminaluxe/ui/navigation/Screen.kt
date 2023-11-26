package com.cencen.luminaluxe.ui.navigation

sealed class Screen (val routes: String) {
    object Main : Screen("main")
    object Cart : Screen("cart")
    object About : Screen("about")
    object DetailSkincare : Screen("main/{skincareId}") {
        fun createRoutes(skincareId: Long) = "main/$skincareId"
    }
}