package com.example.uinavegacion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinavegacion.data.model.Rival
import com.example.uinavegacion.data.repository.RivalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RivalUiState(
    val isLoading: Boolean = false,
    val rivals: List<Rival> = emptyList(),
    val error: String? = null
)

class RivalViewModel(private val rivalRepository: RivalRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RivalUiState())
    val uiState: StateFlow<RivalUiState> = _uiState.asStateFlow()

    init {
        fetchRivales()
    }

    fun fetchRivales() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = rivalRepository.getRivales()

            result.onSuccess { listaRivales ->
                _uiState.update { it.copy(isLoading = false, rivals = listaRivales, error = null) }
            }.onFailure { exception ->
                _uiState.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }
}