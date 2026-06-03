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

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        when {
            email.isBlank() || password.isBlank() ->
                _uiState.value = LoginUiState(errorMessage = "Email dan password tidak boleh kosong")
            !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() ->
                _uiState.value = LoginUiState(errorMessage = "Format email tidak valid")
            else -> viewModelScope.launch {
                _uiState.value = LoginUiState(isLoading = true)
                val result = authRepository.login(email.trim(), password)
                _uiState.value = if (result.isSuccess) {
                    LoginUiState(isSuccess = true)
                } else {
                    LoginUiState(errorMessage = friendlyAuthError(result.exceptionOrNull()?.message))
                }
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)
            val result = authRepository.signInWithGoogle(idToken)
            _uiState.value = if (result.isSuccess) {
                LoginUiState(isSuccess = true)
            } else {
                LoginUiState(errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Google Sign-In gagal")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun friendlyAuthError(message: String?): String = when {
        message == null -> "Login gagal, coba lagi"
        "password is invalid" in message || "wrong-password" in message -> "Password salah"
        "no user record" in message || "user-not-found" in message -> "Email belum terdaftar"
        "email address is badly formatted" in message -> "Format email tidak valid"
        "network error" in message -> "Tidak ada koneksi internet"
        "too-many-requests" in message -> "Terlalu banyak percobaan, coba lagi nanti"
        else -> "Login gagal, coba lagi"
    }
}
