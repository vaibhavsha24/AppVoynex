package com.voynex.app.domain.usecase

import com.voynex.app.domain.repository.DestinationImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Category(val name: String, val imageUrl: String)

class GetCategoriesUseCase(private val repository: DestinationImagesRepository) {

    private val defaultCategories = listOf(
        "Adventure","Beaches", "Mountains","Relaxation","City Break", "Cultural","Romantic",  "Wildlife","Offbeat"
    )

    suspend operator fun invoke(): List<Category> = withContext(Dispatchers.Default) {
        defaultCategories.map {
            val imageUrl = repository.getCoverImage(it)
            Category(it, imageUrl ?: "")
        }
    }
}

class GetCategoryDestinationsUseCase(private val repository: DestinationImagesRepository){


    fun getSuggestedCountriesForCategory(category: String): List<String> {
        val countriesByCategory = mapOf(

            "Adventure" to listOf(
                "New Zealand",
                "Nepal",
                "Iceland",
                "Peru",
                "South Africa",
                "Chile",
                "Canada",
                "Argentina",
                "Australia",
                "Kenya"
            ),

            "Relaxation" to listOf(
                "Maldives",
                "Bali (Indonesia)",
                "Seychelles",
                "Fiji",
                "Greece",
                "Mauritius",
                "Thailand",
                "Hawaii (USA)",
                "Spain",
                "Portugal"
            ),

            "Beaches" to listOf(
                "Philippines",
                "Australia",
                "Thailand",
                "Mexico",
                "Greece",
                "Indonesia",
                "Sri Lanka",
                "Maldives",
                "Brazil",
                "Vietnam"
            ),

            "Mountains" to listOf(
                "Switzerland",
                "Nepal",
                "Canada",
                "Austria",
                "Bhutan",
                "Peru",
                "India",
                "USA (Colorado)",
                "France",
                "New Zealand"
            ),

            "City Break" to listOf(
                "France (Paris)",
                "Japan (Tokyo)",
                "UAE (Dubai)",
                "Singapore",
                "USA (New York)",
                "UK (London)",
                "Spain (Barcelona)",
                "Germany (Berlin)",
                "South Korea (Seoul)",
                "Italy (Rome)"
            ),

            "Cultural" to listOf(
                "India",
                "Egypt",
                "Japan",
                "Turkey",
                "Italy",
                "China",
                "Morocco",
                "Greece",
                "Mexico",
                "Vietnam"
            ),

            "Romantic" to listOf(
                "France (Paris)",
                "Italy (Venice)",
                "Greece (Santorini)",
                "Japan (Kyoto)",
                "French Polynesia (Bora Bora)",
                "Switzerland (Lucerne)",
                "Austria (Hallstatt)",
                "Czech Republic (Prague)",
                "Maldives",
                "Seychelles"
            ),

            "Nightlife" to listOf(
                "Spain (Ibiza)",
                "Netherlands (Amsterdam)",
                "USA (Las Vegas)",
                "Thailand (Bangkok)",
                "Brazil (Rio de Janeiro)",
                "Germany (Berlin)",
                "Mexico (Cancun)",
                "Singapore",
                "UAE (Dubai)",
                "South Korea (Seoul)"
            ),

            "Wildlife" to listOf(
                "Tanzania",
                "Kenya",
                "Botswana",
                "South Africa",
                "India",
                "Australia",
                "Costa Rica",
                "Borneo (Malaysia)",
                "Ecuador (Galapagos)",
                "Brazil (Pantanal)"
            ),

            "Offbeat" to listOf(
                "Bhutan",
                "Georgia",
                "Slovenia",
                "Madagascar",
                "Faroe Islands",
                "Mongolia",
                "Bolivia",
                "Laos",
                "Namibia",
                "Kazakhstan"
            )
        )

        return countriesByCategory[category] ?: emptyList()
    }

    suspend operator fun invoke(category:String): List<HomeDestination> = withContext(Dispatchers.Default) {
        getSuggestedCountriesForCategory(category).map {
            val imageUrl = repository.getCoverImage(it)
            HomeDestination(it, imageUrl ?: "")
        }
    }
}
class GetSavedItinerary(private val repository: DestinationImagesRepository){

    suspend operator fun invoke():List<Category> = withContext(Dispatchers.Default) {
        val list = repository.getSavedItinerary()
        list.map {
            val imageUrl = repository.getCoverImage(it)
            Category(it, imageUrl ?: "")
        }

    }
}