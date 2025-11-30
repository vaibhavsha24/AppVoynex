package com.voynex.app.data.remote.openai

import kotlinx.serialization.Serializable

@Serializable
data class ItineraryResponse(val itinerary: String)

interface OpenAiApi {
    suspend fun generateItinerary(prompt: String): ItineraryResponse
}
