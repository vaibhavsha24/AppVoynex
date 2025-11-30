package com.voynex.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voynex.app.domain.model.Itinerary
import com.voynex.app.domain.usecase.GenerateItineraryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

data class ItineraryUiState(
    val itinerary: Itinerary? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class ItineraryViewModel(private val generateItineraryUseCase: GenerateItineraryUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(ItineraryUiState())
    val uiState: StateFlow<ItineraryUiState> = _uiState

    // Configure Json to ignore unknown keys
    private val json = Json { ignoreUnknownKeys = true }

    fun generateItinerary(tripInput: TripInput) {
        viewModelScope.launch {
            _uiState.value = ItineraryUiState(loading = true)
            try {
                val rawResponse = generateItineraryUseCase(tripInput)
                
                val cleanedJson = rawResponse
                    .substringAfter("```json")
                    .substringBeforeLast("```")
                    .trim()
                    
                val itinerary = json.decodeFromString<Itinerary>(cleanedJson)
                _uiState.value = ItineraryUiState(itinerary = itinerary)
            } catch (e: Exception) {
                _uiState.value = ItineraryUiState(error = e.message)
            }
        }
    }
}