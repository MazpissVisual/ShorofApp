package com.mazpiss.skripsi.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazpiss.skripsi.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(name: String, email: String, password: String) {
        when {
            name.isBlank() || email.isBlank() || password.isBlank() ->
                _uiState.value = RegisterUiState(errorMessage = "Semua field harus diisi")
            !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() ->
                _uiState.value = RegisterUiState(errorMessage = "Format email tidak valid")
            password.length < 6 ->
                _uiState.value = RegisterUiState(errorMessage = "Password minimal 6 karakter")
            else -> viewModelScope.launch {
                _uiState.value = RegisterUiState(isLoading = true)
                val result = authRepository.register(name.trim(), email.trim(), password)
                _uiState.value = if (result.isSuccess) {
                    RegisterUiState(isSuccess = true)
                } else {
                    RegisterUiState(errorMessage = friendlyAuthError(result.exceptionOrNull()?.message))
                }
            }
        }
    }

    private fun friendlyAuthError(message: String?): String = when {
        message == null -> "Registrasi gagal, coba lagi"
        "email address is already in use" in message -> "Email sudah terdaftar, gunakan email lain"
        "email address is badly formatted" in message -> "Format email tidak valid"
        "network error" in message -> "Tidak ada koneksi internet"
        "too-many-requests" in message -> "Terlalu banyak percobaan, coba lagi nanti"
        else -> "Registrasi gagal, coba lagi"
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
