package com.mazpiss.skripsi.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.mazpiss.skripsi.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class ChangePasswordUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun updatePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        if (currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
            _uiState.value = ChangePasswordUiState(errorMessage = "Semua field harus diisi")
            return
        }
        if (newPassword != confirmPassword) {
            _uiState.value = ChangePasswordUiState(errorMessage = "Password baru dan konfirmasi tidak sama")
            return
        }
        if (newPassword.length < 6) {
            _uiState.value = ChangePasswordUiState(errorMessage = "Password baru minimal 6 karakter")
            return
        }

        viewModelScope.launch {
            _uiState.value = ChangePasswordUiState(isLoading = true)
            try {
                val user = authRepository.currentUser
                    ?: throw Exception("Sesi habis, silakan login ulang")
                val email = user.email
                    ?: throw Exception("Akun ini tidak mendukung ganti password via email. Gunakan metode login asli (Google, dll).")
                val credential = EmailAuthProvider.getCredential(email, currentPassword)
                user.reauthenticate(credential).await()
                user.updatePassword(newPassword).await()
                _uiState.value = ChangePasswordUiState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = ChangePasswordUiState(
                    errorMessage = when {
                        e.message?.contains("password is invalid") == true -> "Password saat ini salah"
                        else -> e.localizedMessage ?: "Gagal mengubah password"
                    }
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
