package com.voynex.app.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.voynex.app.domain.model.Itinerary
import androidx.core.content.edit

actual class SharedPref(private val context: Context) {

    private val gson = Gson()
    private val itineraryPref: SharedPreferences =
        context.getSharedPreferences("itinerary_pref", Context.MODE_PRIVATE)

    private val allPrefs: SharedPreferences =
        context.getSharedPreferences("voynex_pref", Context.MODE_PRIVATE)
    actual fun saveItinerary(itinerary: Itinerary) {

        val json = gson.toJson(itinerary)
        itineraryPref.edit { putString(itinerary.tripSummary.destination, json) }
    }

    actual fun loadItinerary(destination:String): Itinerary? {
        val json = itineraryPref.getString(destination, null) ?: return null
        return gson.fromJson(json, Itinerary::class.java)
    }

    actual fun deleteItinerary(destination: String) {
        itineraryPref.edit { remove(destination) }
    }

    actual fun isAnySavedItinerary(): Boolean {
        return itineraryPref.all.isNotEmpty()
    }

    actual fun getAllSavedItineraryNames(): List<String> {
        return itineraryPref.all.keys.toList()
    }

    actual fun getStringOrNull(key: String): String? {
        return allPrefs.getString(key, null)
    }

    /**
     * Saves a string value safely.
     */
    actual fun putString(key: String, value: String) {
        allPrefs.edit { putString(key, value) }
    }

}