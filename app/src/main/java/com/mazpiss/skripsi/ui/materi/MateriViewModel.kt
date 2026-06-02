package com.mazpiss.skripsi.ui.materi

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MateriUiState(
    val isLoading: Boolean = false,
    val materiList: List<Materi> = emptyList()
)

class MateriViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow(MateriUiState())
    val uiState: StateFlow<MateriUiState> = _uiState.asStateFlow()

    init {
        fetchMateriData()
    }

    private fun fetchMateriData() {
        _uiState.update { it.copy(isLoading = true) }
        db.collection("Materi").orderBy("totalPembahasan", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _uiState.update { it.copy(isLoading = false) }
                    return@addSnapshotListener
                }
                val list = value?.documents?.mapNotNull { it.toObject(Materi::class.java) } ?: emptyList()
                _uiState.update { it.copy(isLoading = false, materiList = list) }
            }
    }
}
