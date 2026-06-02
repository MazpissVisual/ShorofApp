package com.mazpiss.skripsi.ui.quiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mazpiss.skripsi.ui.quizList.QuizListActivty
import com.mazpiss.skripsi.ui.theme.ShorofTheme

class QuizActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShorofTheme {
                QuizScreen(
                    onQuizClick = { quiz ->
                        QuizListActivty.questionModelList = quiz.questionList
                        QuizListActivty.time = quiz.time
                        startActivity(Intent(this, QuizListActivty::class.java))
                    }
                )
            }
        }
    }
}
