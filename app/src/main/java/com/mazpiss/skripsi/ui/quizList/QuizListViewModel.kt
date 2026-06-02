package com.mazpiss.skripsi.ui.quizList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazpiss.skripsi.ui.quiz.QuestionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizListUiState(
    val questions: List<QuestionModel> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String = "",
    val score: Int = 0,
    val timeRemaining: Long = 0L,
    val isFinished: Boolean = false
)

class QuizListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuizListUiState())
    val uiState: StateFlow<QuizListUiState> = _uiState.asStateFlow()

    fun startQuiz(questions: List<QuestionModel>, time: String) {
        _uiState.update { it.copy(questions = questions) }
        startTimer(time.toInt())
    }

    private fun startTimer(totalMinutes: Int) {
        val totalMs = totalMinutes * 60 * 1000L
        viewModelScope.launch(Dispatchers.Main) {
            for (time in totalMs downTo 0 step 1000L) {
                _uiState.update { it.copy(timeRemaining = time) }
                delay(1000L)
            }
            _uiState.update { it.copy(isFinished = true) }
        }
    }

    fun selectAnswer(answer: String) {
        _uiState.update { it.copy(selectedAnswer = answer) }
    }

    fun nextQuestion(): Boolean {
        val state = _uiState.value
        if (state.selectedAnswer.isEmpty()) return false

        var newScore = state.score
        if (state.selectedAnswer == state.questions[state.currentIndex].correct) {
            newScore += 1
        }

        if (state.currentIndex < state.questions.size - 1) {
            _uiState.update {
                it.copy(score = newScore, currentIndex = it.currentIndex + 1, selectedAnswer = "")
            }
        } else {
            _uiState.update { it.copy(score = newScore, isFinished = true) }
        }
        return true
    }
}
