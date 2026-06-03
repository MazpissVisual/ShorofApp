package com.mazpiss.skripsi.ui.profile

import androidx.lifecycle.ViewModel
import com.mazpiss.skripsi.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ProfileUiState(
    val displayName: String = "",
    val email: String = "",
    val isLoggedOut: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val user = authRepository.currentUser
        _uiState.value = ProfileUiState(
            displayName = user?.displayName ?: "Pengguna",
            email = user?.email ?: ""
        )
    }

    fun logout() {
        authRepository.logout()
        _uiState.value = _uiState.value.copy(isLoggedOut = true)
    }
}
