package com.voynex.app.domain.repository

/**
 * Repository interface for fetching destination images.
 */
interface DestinationImagesRepository {

    /**
     * Fetches a single cover image URL for a given destination, for use on overview screens.
     * Implements caching.
     */
    suspend fun getCoverImage(destination: String): String?

    /**
     * Fetches a list of 10 image URLs for a given destination, for use on detail screens.
     * Implements caching.
     */
    suspend fun getDestinationPhotos(destination: String): List<String>
}
