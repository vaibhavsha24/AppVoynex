package com.voynex.app.data.remote.pexels

interface PexelsApi {
    suspend fun searchImages(query: String, perPage: Int = 10): PexelsSearchResponseDto
}
