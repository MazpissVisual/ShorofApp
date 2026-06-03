package com.mazpiss.skripsi.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.ui.auth.ForgotPasswordScreen
import com.mazpiss.skripsi.ui.auth.LoginScreen
import com.mazpiss.skripsi.ui.auth.RegisterScreen
import com.mazpiss.skripsi.ui.aboutApp.AboutScreen
import com.mazpiss.skripsi.ui.profile.BadgeCollectionScreen
import com.mazpiss.skripsi.ui.profile.ChangePasswordScreen
import com.mazpiss.skripsi.ui.profile.EditProfileScreen
import com.mazpiss.skripsi.ui.profile.ProfileScreen
import com.mazpiss.skripsi.ui.quiz.QuizDetailScreen
import com.mazpiss.skripsi.ui.quiz.QuizResultScreen
import com.mazpiss.skripsi.ui.faq.FaqScreen
import com.mazpiss.skripsi.ui.home.HomeScreen
import com.mazpiss.skripsi.ui.kosakata.KosakataScreen
import com.mazpiss.skripsi.ui.materi.Materi
import com.mazpiss.skripsi.ui.materi.MateriScreen
import com.mazpiss.skripsi.ui.materiDetail.DetailMateriScreen
import com.mazpiss.skripsi.ui.materiShorof.MateriShorofScreen
import com.mazpiss.skripsi.ui.onboarding.OnboardingScreen
import com.mazpiss.skripsi.ui.quiz.QuizScreen
import com.mazpiss.skripsi.ui.quizList.QuizListScreen
import com.mazpiss.skripsi.ui.test.TestScreen

private val bottomNavScreens = setOf(
    Screen.Home.route,
    Screen.Materi.route,
    Screen.Quiz.route,
    Screen.Kosakata.route
)

@Composable
fun ShorofApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavScreens) {
                ShorofBottomNavigation(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { paddingValues ->
        ShorofNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun ShorofNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route,
        modifier = modifier
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAbout = { navController.navigate(Screen.Profile.route) }
            )
        }

        composable(Screen.Materi.route) {
            MateriScreen(
                onMateriClick = { materi: Materi ->
                    navController.navigate(Screen.DetailMateri.createRoute(materi.judul ?: ""))
                }
            )
        }

        composable(Screen.Quiz.route) {
            TestScreen(
                onStartQuiz = { navController.navigate(Screen.QuizScreen.route) },
                onViewRules = { navController.navigate(Screen.Faq.route) }
            )
        }

        composable(Screen.Kosakata.route) {
            KosakataScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onEditProfile = { navController.navigate(Screen.EditProfile.route) },
                onBadgesClick = { navController.navigate(Screen.BadgeCollection.route) },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onChangePassword = { navController.navigate(Screen.ChangePassword.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(
                onBackClick = { navController.popBackStack() },
                onForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(Screen.BadgeCollection.route) {
            BadgeCollectionScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.QuizDetail.route,
            arguments = listOf(navArgument("quizId") { type = NavType.StringType })
        ) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getString("quizId") ?: ""
            QuizDetailScreen(
                onBackClick = { navController.popBackStack() },
                onStartQuiz = {
                    navController.navigate(Screen.QuizList.createRoute(quizId))
                }
            )
        }

        composable(Screen.About.route) {
            AboutScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.Faq.route) {
            FaqScreen(onBackClick = { navController.popBackStack() })
        }

        composable(
            route = Screen.DetailMateri.route,
            arguments = listOf(navArgument("materiJudul") { type = NavType.StringType })
        ) { backStackEntry ->
            val materiJudul = backStackEntry.arguments?.getString("materiJudul")
            DetailMateriScreen(
                materiJudul = materiJudul,
                onBackClick = { navController.popBackStack() },
                onSubMateriClick = { subMateri ->
                    navController.navigate(
                        Screen.MateriShorof.createRoute(subMateri.subJudul, subMateri.content)
                    )
                }
            )
        }

        composable(
            route = Screen.MateriShorof.route,
            arguments = listOf(
                navArgument("subJudul") { type = NavType.StringType },
                navArgument("content") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val subJudul = backStackEntry.arguments?.getString("subJudul") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""
            MateriShorofScreen(
                subJudul = subJudul,
                content = content,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.QuizScreen.route) {
            QuizScreen(
                onQuizClick = { quiz ->
                    navController.navigate(Screen.QuizDetail.createRoute(quiz.id))
                }
            )
        }

        composable(
            route = Screen.QuizList.route,
            arguments = listOf(navArgument("quizId") { type = NavType.StringType })
        ) {
            QuizListScreen(
                onFinish = { score, total ->
                    navController.navigate(Screen.QuizResult.createRoute(score, total)) {
                        popUpTo(Screen.QuizList.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.QuizResult.route,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0
            QuizResultScreen(
                score = score,
                total = total,
                onContinue = {
                    navController.popBackStack(Screen.Quiz.route, inclusive = false)
                },
                onRetry = {
                    navController.popBackStack(Screen.QuizList.route, inclusive = false)
                }
            )
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
    currentRoute: String?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.screen.route
            NavigationBarItem(
                selected = isSelected,
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
                    Text(
                        text = stringResource(id = item.labelRes),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
