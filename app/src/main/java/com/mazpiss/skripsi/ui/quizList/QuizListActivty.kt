package com.mazpiss.skripsi.ui.quizList

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mazpiss.skripsi.ui.quiz.QuestionModel
import com.mazpiss.skripsi.ui.theme.ShorofTheme

class QuizListActivty : ComponentActivity() {

    companion object {
        var questionModelList: List<QuestionModel> = listOf()
        var time: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShorofTheme {
                QuizListScreen(
                    questions = questionModelList,
                    time = time,
                    onFinish = { finish() }
                )
            }
        }
    }
}
