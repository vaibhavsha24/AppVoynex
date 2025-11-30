package com.voynex.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voynex.app.domain.repository.DestinationImagesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DestinationUiState(
    val images: List<String> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class DestinationViewModel(
    private val repository: DestinationImagesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DestinationUiState())
    val uiState: StateFlow<DestinationUiState> = _uiState

    fun loadImages(destination: String) {
        viewModelScope.launch {
            _uiState.value = DestinationUiState(loading = true)
            try {
                val data = repository.getDestinationPhotos(destination)
                _uiState.value = DestinationUiState(images = data)
            } catch (e: Exception) {
                _uiState.value = DestinationUiState(error = e.message)
            }
        }
    }
}