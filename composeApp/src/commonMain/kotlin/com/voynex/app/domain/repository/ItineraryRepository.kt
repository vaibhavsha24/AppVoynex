package com.voynex.app.domain.repository

interface ItineraryRepository {
    suspend fun generateItinerary(prompt: String): String
}
