package com.mazpiss.skripsi.ui.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.ui.aboutApp.AboutActivity
import com.mazpiss.skripsi.ui.faq.FaqActivity
import com.mazpiss.skripsi.ui.home.HomeScreen
import com.mazpiss.skripsi.ui.kosakata_fragment.KosakataScreen
import com.mazpiss.skripsi.ui.materi.Materi
import com.mazpiss.skripsi.ui.materi.MateriScreen
import com.mazpiss.skripsi.ui.materiDetail.DetailMateriActivity
import com.mazpiss.skripsi.ui.quiz.QuizActivity
import com.mazpiss.skripsi.ui.test.TestScreen

@Composable
fun ShorofApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            ShorofBottomNavigation(navController = navController)
        }
    ) { paddingValues ->
        ShorofNavHost(
            navController = navController,
            context = context,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun ShorofNavHost(
    navController: NavHostController,
    context: Context,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAbout = {
                    context.startActivity(Intent(context, AboutActivity::class.java))
                }
            )
        }
        composable(Screen.Materi.route) {
            MateriScreen(
                onMateriClick = { materi: Materi ->
                    val intent = Intent(context, DetailMateriActivity::class.java).apply {
                        putExtra("urutan", materi.judul)
                    }
                    context.startActivity(intent)
                }
            )
        }
        composable(Screen.Quiz.route) {
            TestScreen(
                onStartQuiz = {
                    context.startActivity(Intent(context, QuizActivity::class.java))
                },
                onViewRules = {
                    context.startActivity(Intent(context, FaqActivity::class.java))
                }
            )
        }
        composable(Screen.Kosakata.route) {
            KosakataScreen()
        }
    }
}

private data class BottomNavItem(
    val screen: Screen,
    val iconRes: Int,
    val labelRes: Int
)

private val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, R.drawable.home, R.string.title_home),
    BottomNavItem(Screen.Materi, R.drawable.book_open, R.string.title_materi),
    BottomNavItem(Screen.Quiz, R.drawable.li_pie_chart, R.string.title_quiz),
    BottomNavItem(Screen.Kosakata, R.drawable.info, R.string.kosakata)
)

@Composable
private fun ShorofBottomNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = stringResource(id = item.labelRes)
                    )
                },
                label = {
                    Text(text = stringResource(id = item.labelRes))
                }
            )
        }
    }
}
