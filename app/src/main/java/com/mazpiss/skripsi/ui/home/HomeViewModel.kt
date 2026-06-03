package com.mazpiss.skripsi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazpiss.skripsi.domain.model.UserProgress
import com.mazpiss.skripsi.domain.repository.AuthRepository
import com.mazpiss.skripsi.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val displayName: String = "",
    val progress: UserProgress = UserProgress()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val userId get() = authRepository.currentUser?.uid ?: ""

    init {
        val name = authRepository.currentUser?.displayName?.ifBlank { "Kamu" } ?: "Kamu"
        _uiState.update { it.copy(displayName = name) }
        fetchProgress()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            progressRepository.getUserProgress(userId)
                .catch { _uiState.update { it.copy(isRefreshing = false) } }
                .collect { progress ->
                    _uiState.update { it.copy(isRefreshing = false, progress = progress) }
                }
        }
    }

    private fun fetchProgress() {
        if (userId.isEmpty()) return
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            progressRepository.getUserProgress(userId)
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { progress ->
                    _uiState.update { it.copy(isLoading = false, progress = progress) }
                }
        }
    }
}
