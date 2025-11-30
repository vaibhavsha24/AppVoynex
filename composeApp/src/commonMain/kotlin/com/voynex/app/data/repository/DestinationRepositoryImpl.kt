package com.voynex.app.data.repository

import com.russhwolf.settings.Settings
import com.voynex.app.data.mapper.toDomain
import com.voynex.app.data.remote.pexels.PexelsApi
import com.voynex.app.domain.repository.DestinationImagesRepository
import com.voynex.app.preferences.SharedPref
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DestinationRepositoryImpl(
    private val pexelsApi: PexelsApi,
    private val sharedPref: SharedPref
) : DestinationImagesRepository {

    private val coverImageCacheKey = { destination: String -> "${destination}_cover" }
    private val photoListCacheKey = { destination: String -> "${destination}_photos" }

    override suspend fun getCoverImage(destination: String): String? {
        sharedPref.getStringOrNull(coverImageCacheKey(destination))?.let { return it }

        val newImage = pexelsApi.searchImages(destination, 1).toDomain().firstOrNull()
        if (newImage != null) {
            sharedPref.putString(coverImageCacheKey(destination), newImage)
            return newImage
        }

        return null
    }

    override suspend fun getDestinationPhotos(destination: String): List<String> {
        sharedPref.getStringOrNull(photoListCacheKey(destination))?.let {
            return Json.decodeFromString(it)
        }

        val newImages = pexelsApi.searchImages(destination, 10).toDomain()
        if (newImages.isNotEmpty()) {
            sharedPref.putString(photoListCacheKey(destination), Json.encodeToString(newImages))
            return newImages
        }

        return emptyList()
    }

    override suspend fun getSavedItinerary(): List<String> {
        if(sharedPref.isAnySavedItinerary()){
            return sharedPref.getAllSavedItineraryNames()
        }else{
            return emptyList()
        }
    }
}
