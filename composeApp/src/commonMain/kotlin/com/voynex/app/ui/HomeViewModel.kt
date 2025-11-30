package com.voynex.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voynex.app.domain.model.Itinerary
import com.voynex.app.domain.usecase.Category
import com.voynex.app.domain.usecase.GetCategoriesUseCase
import com.voynex.app.domain.usecase.GetHomeDestinationsUseCase
import com.voynex.app.domain.usecase.GetSavedItinerary
import com.voynex.app.domain.usecase.HomeDestination
import com.voynex.app.preferences.SharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val destinations: List<HomeDestination> = emptyList(),
    val categories: List<Category> = emptyList(),
    val savedItinerary:List<Category> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val getHomeDestinations: GetHomeDestinationsUseCase,
    private val getCategories: GetCategoriesUseCase,
    private val getSavedItinerary: GetSavedItinerary,
    private val sharedPref: SharedPref
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState
    private val _categoryUiState = MutableStateFlow(HomeUiState())

    val categoryUiState: StateFlow<HomeUiState> = _categoryUiState
    init {
        loadHomeContent()
    }

    fun loadHomeContent() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(loading = true)
            try {
                val destinations = getHomeDestinations()
                val categories = getCategories()
                val savedItinerary = getSavedItinerary()
                _uiState.value = HomeUiState(destinations = destinations, categories = categories, savedItinerary = savedItinerary)
            } catch (e: Exception) {
                _uiState.value = HomeUiState(error = e.message)
            }
        }
    }

    fun getItinerary(destinaton:String): Itinerary?{
        return sharedPref.loadItinerary(destinaton)
    }
}