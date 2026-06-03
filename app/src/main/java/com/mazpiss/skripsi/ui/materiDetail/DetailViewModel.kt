package com.mazpiss.skripsi.ui.materiDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazpiss.skripsi.domain.repository.AuthRepository
import com.mazpiss.skripsi.domain.repository.MateriRepository
import com.mazpiss.skripsi.domain.repository.ProgressRepository
import com.mazpiss.skripsi.ui.materiDetail.MateriDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val isLoading: Boolean = false,
    val subMateriList: List<MateriDetail> = emptyList()
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val materiRepository: MateriRepository,
    private val progressRepository: ProgressRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val userId get() = authRepository.currentUser?.uid ?: ""

    fun loadSubMateri(judul: String?) {
        if (judul.isNullOrBlank()) return
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            materiRepository.getSubMateri(judul)
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { list ->
                    _uiState.update { it.copy(isLoading = false, subMateriList = list) }
                    saveMateriProgress(judul, list.size)
                }
        }
    }

    private fun saveMateriProgress(materiId: String, total: Int) {
        if (userId.isEmpty() || total == 0) return
        viewModelScope.launch {
            progressRepository.updateMateriProgress(
                userId = userId,
                materiId = materiId,
                selesai = 1,
                total = total
            )
        }
    }
}
