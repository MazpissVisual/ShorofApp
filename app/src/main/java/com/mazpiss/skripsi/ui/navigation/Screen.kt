package com.mazpiss.skripsi.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Materi : Screen("materi")
    object Quiz : Screen("quiz")
    object Kosakata : Screen("kosakata")
}
