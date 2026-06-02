package com.mazpiss.skripsi.ui.kosakata_fragment

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mazpiss.skripsi.ui.kosakata.Kosakata
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class KosakataUiState(
    val isLoading: Boolean = false,
    val kosakataList: List<Kosakata> = emptyList()
)

class KosakataViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow(KosakataUiState())
    val uiState: StateFlow<KosakataUiState> = _uiState.asStateFlow()

    init {
        fetchKosakata()
    }

    private fun fetchKosakata() {
        _uiState.update { it.copy(isLoading = true) }
        db.collection("Kosakata").orderBy("Terjemahan", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _uiState.update { it.copy(isLoading = false) }
                    return@addSnapshotListener
                }
                val list = value?.documents?.mapNotNull { it.toObject(Kosakata::class.java) } ?: emptyList()
                _uiState.update { it.copy(isLoading = false, kosakataList = list) }
            }
    }
}
