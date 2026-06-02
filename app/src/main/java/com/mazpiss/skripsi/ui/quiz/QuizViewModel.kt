package com.mazpiss.skripsi.ui.quiz

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class QuizUiState(
    val isLoading: Boolean = false,
    val quizList: List<QuizModel> = emptyList(),
    val error: String? = null
)

class QuizViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        getDataFromFirebase()
    }

    private fun getDataFromFirebase() {
        _uiState.update { it.copy(isLoading = true) }
        FirebaseDatabase.getInstance().reference.child("quizzes")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = mutableListOf<QuizModel>()
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        try {
                            val model = child.getValue(QuizModel::class.java)
                            if (model != null) list.add(model)
                        } catch (e: DatabaseException) {
                            _uiState.update { it.copy(error = "Gagal memuat data") }
                        }
                    }
                }
                _uiState.update { it.copy(isLoading = false, quizList = list) }
            }
            .addOnFailureListener {
                _uiState.update { it.copy(isLoading = false, error = "Error mengambil data") }
            }
    }
}
