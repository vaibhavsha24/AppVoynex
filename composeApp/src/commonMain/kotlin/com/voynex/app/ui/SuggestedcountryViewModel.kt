package com.voynex.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voynex.app.domain.usecase.Category
import com.voynex.app.domain.usecase.GetCategoriesUseCase
import com.voynex.app.domain.usecase.GetCategoryDestinationsUseCase
import com.voynex.app.domain.usecase.GetHomeDestinationsUseCase
import com.voynex.app.domain.usecase.HomeDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SuggestedCountryUiState(
    val destinations: List<HomeDestination> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class SuggestedCountryViewModel(
    private val getCategoryDestinations: GetCategoryDestinationsUseCase
) : ViewModel() {

     val _categoryUiState = MutableStateFlow(HomeUiState())

    val categoryUiState: StateFlow<HomeUiState> = _categoryUiState


     fun loadCategoryUiState(category:String) {
         viewModelScope.launch {
             _categoryUiState.value = HomeUiState(loading = true)
             try {
                 val categoryDestination = getCategoryDestinations(category)
                 _categoryUiState.value = HomeUiState(destinations = categoryDestination)
             } catch (e: Exception) {
                 _categoryUiState.value = HomeUiState(error = e.message)
             }
        }
    }
}