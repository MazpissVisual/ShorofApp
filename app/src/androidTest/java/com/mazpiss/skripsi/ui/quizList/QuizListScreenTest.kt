package com.mazpiss.skripsi.ui.quizList

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mazpiss.skripsi.ui.quiz.QuestionModel
import com.mazpiss.skripsi.ui.theme.ShorofTheme
import org.junit.Rule
import org.junit.Test

class QuizListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val dummyQuestions = listOf(
        QuestionModel(
            question = "Apa itu Fi'il Madhi?",
            options = listOf("Kata kerja lampau", "Kata benda", "Kata sifat", "Partikel"),
            correct = "Kata kerja lampau"
        )
    )

    @Test
    fun quizListContent_menampilkan_soal_pertama() {
        composeTestRule.setContent {
            ShorofTheme {
                QuizListContent(
                    uiState = QuizListUiState(
                        questions = dummyQuestions,
                        currentIndex = 0,
                        timeRemaining = 600_000L
                    ),
                    onSelectAnswer = {},
                    onNextQuestion = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Apa itu Fi'il Madhi?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Kata kerja lampau").assertIsDisplayed()
    }

    @Test
    fun quizListContent_klik_jawaban_memilih_opsi() {
        var selectedAnswer = ""

        composeTestRule.setContent {
            ShorofTheme {
                QuizListContent(
                    uiState = QuizListUiState(
                        questions = dummyQuestions,
                        currentIndex = 0,
                        timeRemaining = 600_000L
                    ),
                    onSelectAnswer = { selectedAnswer = it },
                    onNextQuestion = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Kata kerja lampau").performClick()
        assert(selectedAnswer == "Kata kerja lampau")
    }
}
