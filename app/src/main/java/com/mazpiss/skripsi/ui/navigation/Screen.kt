package com.mazpiss.skripsi.ui.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Materi : Screen("materi")
    object Quiz : Screen("quiz")
    object Kosakata : Screen("kosakata")
    object About : Screen("about")
    object Faq : Screen("faq")
    object QuizScreen : Screen("quiz_screen")

    object DetailMateri : Screen("detail_materi/{materiJudul}") {
        fun createRoute(materiJudul: String) = "detail_materi/${Uri.encode(materiJudul)}"
    }

    object MateriShorof : Screen("materi_shorof/{subJudul}/{content}") {
        fun createRoute(subJudul: String, content: String) =
            "materi_shorof/${Uri.encode(subJudul)}/${Uri.encode(content)}"
    }

    object QuizList : Screen("quiz_list/{quizId}") {
        fun createRoute(quizId: String) = "quiz_list/$quizId"
    }

    object QuizDetail : Screen("quiz_detail/{quizId}") {
        fun createRoute(quizId: String) = "quiz_detail/$quizId"
    }

    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object BadgeCollection : Screen("badge_collection")
    object ChangePassword : Screen("change_password")
    object ForgotPassword : Screen("forgot_password")
    object QuizResult : Screen("quiz_result/{score}/{total}") {
        fun createRoute(score: Int, total: Int) = "quiz_result/$score/$total"
    }
}
