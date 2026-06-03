package com.mazpiss.skripsi.ui.kosakata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazpiss.skripsi.domain.repository.KosakataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class KosakataUiState(
    val isLoading: Boolean = false,
    val kosakataList: List<Kosakata> = emptyList()
)

@HiltViewModel
class KosakataViewModel @Inject constructor(
    private val kosakataRepository: KosakataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KosakataUiState())
    val uiState: StateFlow<KosakataUiState> = _uiState.asStateFlow()

    init {
        fetchKosakata()
    }

    private fun fetchKosakata() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            kosakataRepository.getKosakata()
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { list ->
                    _uiState.update { it.copy(isLoading = false, kosakataList = list) }
                }
        }
    }
}
