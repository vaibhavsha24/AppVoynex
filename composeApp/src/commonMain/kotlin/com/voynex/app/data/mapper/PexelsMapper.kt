package com.voynex.app.data.mapper

import com.voynex.app.data.remote.pexels.PexelsSearchResponseDto

fun PexelsSearchResponseDto.toDomain(): List<String> {
    return photos.map { it.src.large2x }
}
