package com.mazpiss.skripsi.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazpiss.skripsi.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val isEmailSent: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun sendResetEmail(email: String) {
        if (email.isBlank()) {
            _uiState.value = ForgotPasswordUiState(errorMessage = "Email tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            _uiState.value = ForgotPasswordUiState(isLoading = true)
            val result = authRepository.resetPassword(email.trim())
            _uiState.value = if (result.isSuccess) {
                ForgotPasswordUiState(isEmailSent = true)
            } else {
                ForgotPasswordUiState(
                    errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Gagal mengirim email"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
