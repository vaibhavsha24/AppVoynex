package com.voynex.app.data.remote.pexels

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun httpClientEngine(): HttpClientEngine

class PexelsClient : PexelsApi {

    private val client = HttpClient(httpClientEngine()) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    override suspend fun searchImages(query: String, perPage: Int): PexelsSearchResponseDto {
        return client.get("https://api.pexels.com/v1/search") {
            url {
                parameters.append("query", query)
                parameters.append("per_page", perPage.toString())
            }
            headers {
                append("Authorization", "AH1L5sb6IVNrlzMQet6Gri87xc4iZrjM9ht4QCmKzAXOsSElpFBdgNQC")
            }
        }.body()
    }
}
