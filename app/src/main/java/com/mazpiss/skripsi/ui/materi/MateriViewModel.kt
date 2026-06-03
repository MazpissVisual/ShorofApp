package com.mazpiss.skripsi.ui.materi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazpiss.skripsi.domain.repository.MateriRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MateriUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val materiList: List<Materi> = emptyList()
)

@HiltViewModel
class MateriViewModel @Inject constructor(
    private val materiRepository: MateriRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MateriUiState())
    val uiState: StateFlow<MateriUiState> = _uiState.asStateFlow()

    init {
        fetchMateri()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            materiRepository.getMateri()
                .catch { _uiState.update { it.copy(isRefreshing = false) } }
                .collect { list ->
                    _uiState.update { it.copy(isRefreshing = false, materiList = list) }
                }
        }
    }

    private fun fetchMateri() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            materiRepository.getMateri()
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { list ->
                    _uiState.update { it.copy(isLoading = false, materiList = list) }
                }
        }
    }
}
