package com.voynex.app.data.repository

import com.voynex.app.data.remote.openai.OpenAiApi
import com.voynex.app.domain.repository.ItineraryRepository

class ItineraryRepositoryImpl(private val openAiApi: OpenAiApi) : ItineraryRepository {
    override suspend fun generateItinerary(prompt: String): String {
        return openAiApi.generateItinerary(prompt).itinerary
    }
}
