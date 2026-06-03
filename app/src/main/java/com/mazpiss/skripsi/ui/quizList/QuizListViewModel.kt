package com.mazpiss.skripsi.ui.quizList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazpiss.skripsi.domain.repository.AuthRepository
import com.mazpiss.skripsi.domain.repository.ProgressRepository
import com.mazpiss.skripsi.domain.repository.QuizRepository
import com.mazpiss.skripsi.ui.quiz.QuestionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizListUiState(
    val isLoading: Boolean = false,
    val questions: List<QuestionModel> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String = "",
    val score: Int = 0,
    val timeRemaining: Long = 0L,
    val isFinished: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class QuizListViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val progressRepository: ProgressRepository,
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId get() = authRepository.currentUser?.uid ?: ""
    private var currentQuizId: String = ""

    private val _uiState = MutableStateFlow(QuizListUiState())
    val uiState: StateFlow<QuizListUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("quizId")?.let {
            currentQuizId = it
            loadQuiz(it)
        }
    }

    private fun loadQuiz(quizId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val quiz = quizRepository.getQuizById(quizId)
                if (quiz != null) {
                    _uiState.update { it.copy(isLoading = false, questions = quiz.questionList) }
                    startTimer(quiz.time.toIntOrNull() ?: 0)
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Quiz tidak ditemukan") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Gagal memuat quiz") }
            }
        }
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
            saveProgress(newScore, state.questions.size)
        }
        return true
    }

    private fun saveProgress(score: Int, total: Int) {
        if (userId.isEmpty()) return
        viewModelScope.launch {
            progressRepository.saveQuizResult(userId, currentQuizId, score, total)
        }
    }
}
