package com.voynex.app.domain.usecase

import com.voynex.app.domain.repository.DestinationImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class HomeDestination(val name: String, val imageUrl: String)

class GetHomeDestinationsUseCase(private val repository: DestinationImagesRepository) {

    private val defaultDestinations = listOf(
        "Jaipur",
        "Goa",
        "Delhi",
        "Bali",
        "Dubai",
        "London",
        "Paris",
        "Singapore",

        "New York",
        "Tokyo",
        "Bangkok",
        "Sydney",
        "Rome",
        "Barcelona",
        "Istanbul",
        "Phuket",
        "Maldives",
        "Switzerland",
        "Amsterdam",
        "Vienna",
        "Cape Town"
    )

    suspend operator fun invoke(): List<HomeDestination> = withContext(Dispatchers.Default) {
        defaultDestinations.map {
            val imageUrl = repository.getCoverImage(it)
            HomeDestination(it, imageUrl ?: "")
        }
    }
}