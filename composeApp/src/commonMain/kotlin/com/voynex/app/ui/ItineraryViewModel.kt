package com.voynex.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import com.voynex.app.domain.model.Itinerary
import com.voynex.app.domain.usecase.GenerateItineraryUseCase
import com.voynex.app.domain.usecase.GetCoverImage
import com.voynex.app.preferences.SharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

data class ItineraryUiState(
    val itinerary: Itinerary? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class ItineraryViewModel(private val generateItineraryUseCase: GenerateItineraryUseCase,private val getCoverImage: GetCoverImage,private val sharedPref: SharedPref) : ViewModel() {

    private val _uiState = MutableStateFlow(ItineraryUiState())
    val uiState: StateFlow<ItineraryUiState> = _uiState

    // Configure Json to ignore unknown keys
    private val json = Json { ignoreUnknownKeys = true }

    private var tripInput: TripInput? = null
    fun generateItinerary(tripInput: TripInput) {
        this.tripInput = tripInput
        viewModelScope.launch {
            _uiState.value = ItineraryUiState(loading = true)
            try {
                val image= getCoverImage(tripInput.destination)
                val rawResponse = generateItineraryUseCase(tripInput)

                val cleanedJson = rawResponse
                    .substringAfter("```json")
                    .substringBeforeLast("```")
                    .trim()
                    
                val itinerary = json.decodeFromString<Itinerary>(cleanedJson)
                itinerary.tripSummary.coverImage = image.imageUrl
                _uiState.value = ItineraryUiState(itinerary = itinerary)
            } catch (e: Exception) {
                _uiState.value = ItineraryUiState(error = e.message)
            }
        }
    }

    fun generateSavedItinerary(destination: String){
        _uiState.value = ItineraryUiState(loading = true)
        val itinerary = sharedPref.loadItinerary(destination)
        _uiState.value = ItineraryUiState(itinerary = itinerary)
    }
    fun saveOffline(){
        uiState.value.itinerary?.let { itinerary ->
            sharedPref.saveItinerary(itinerary)
        }
    }

    fun deleteOffline(){
        _uiState.value.itinerary?.tripSummary?.destination?.let { destination ->
            sharedPref.deleteItinerary(destination);
        }

    }
}