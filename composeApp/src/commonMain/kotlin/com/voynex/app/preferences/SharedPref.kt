package com.voynex.app.preferences



import com.voynex.app.domain.model.Itinerary

expect class SharedPref {

    fun saveItinerary(itinerary: Itinerary)

    fun loadItinerary(destination: String): Itinerary?

    fun deleteItinerary(destination: String)

    fun isAnySavedItinerary(): Boolean

    fun getAllSavedItineraryNames(): List<String>
     fun getStringOrNull(key: String): String?


    fun putString(key: String, value: String)
}

