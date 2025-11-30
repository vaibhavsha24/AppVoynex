package com.voynex.app.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.russhwolf.settings.Settings
import com.voynex.app.data.remote.openai.MockOpenAiApi
import com.voynex.app.data.remote.openai.OpenAiApiImpl
import com.voynex.app.data.remote.pexels.PexelsClient
import com.voynex.app.data.repository.DestinationRepositoryImpl
import com.voynex.app.data.repository.ItineraryRepositoryImpl
import com.voynex.app.domain.usecase.GenerateItineraryUseCase
import com.voynex.app.domain.usecase.GetCategoriesUseCase
import com.voynex.app.domain.usecase.GetCategoryDestinationsUseCase
import com.voynex.app.domain.usecase.GetHomeDestinationsUseCase
import com.voynex.app.ui.DestinationViewModel
import com.voynex.app.ui.HomeViewModel
import com.voynex.app.ui.ItineraryViewModel
import com.voynex.app.ui.SuggestedCountryViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val settings: Settings) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DestinationViewModel::class.java)) {
            val pexelsApi = PexelsClient()
            val destinationRepository = DestinationRepositoryImpl(pexelsApi, settings)
            return DestinationViewModel(destinationRepository) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val pexelsApi = PexelsClient()
            val destinationRepository = DestinationRepositoryImpl(pexelsApi, settings)
            val getHomeDestinationsUseCase = GetHomeDestinationsUseCase(destinationRepository)
            val getCategoriesUseCase = GetCategoriesUseCase(destinationRepository)
            val getCategoryDestinationsUseCase = GetCategoryDestinationsUseCase(destinationRepository)

            return HomeViewModel(getHomeDestinationsUseCase, getCategoriesUseCase,getCategoryDestinationsUseCase) as T
        }
        if (modelClass.isAssignableFrom(ItineraryViewModel::class.java)) {
            val openAiApi = OpenAiApiImpl()
            val itineraryRepository = ItineraryRepositoryImpl(openAiApi)
            val generateItineraryUseCase = GenerateItineraryUseCase(itineraryRepository)
            return ItineraryViewModel(generateItineraryUseCase) as T
        }
        if (modelClass.isAssignableFrom(SuggestedCountryViewModel::class.java)) {
            val pexelsApi = PexelsClient()
            val destinationRepository = DestinationRepositoryImpl(pexelsApi, settings)
            val getCategoryDestinationsUseCase = GetCategoryDestinationsUseCase(destinationRepository)
            return SuggestedCountryViewModel(getCategoryDestinationsUseCase) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}