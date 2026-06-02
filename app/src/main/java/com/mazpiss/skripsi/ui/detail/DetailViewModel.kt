package com.mazpiss.skripsi.ui.detail

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.mazpiss.skripsi.ui.materiDetail.MateriDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DetailUiState(
    val isLoading: Boolean = false,
    val subMateriList: List<MateriDetail> = emptyList()
)

class DetailViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadSubMateri(judul: String?) {
        _uiState.update { it.copy(isLoading = true) }
        db.collection("subMateri").whereEqualTo("judul", judul).get()
            .addOnSuccessListener { documents ->
                val list = documents.mapNotNull { it.toObject(MateriDetail::class.java) }
                    .sortedBy { it.subJudul }
                _uiState.update { it.copy(isLoading = false, subMateriList = list) }
            }
            .addOnFailureListener {
                _uiState.update { it.copy(isLoading = false) }
            }
    }
}
