package com.voynex.app.data.remote.openai

import com.voynex.app.data.remote.ApiKey // Import the ApiKey
import com.voynex.app.data.remote.pexels.httpClientEngine
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class OpenAiApiImpl : OpenAiApi {

    private val client = HttpClient(httpClientEngine()) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        install(Logging) {
            level = LogLevel.ALL
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 120_000   // 120 sec
            socketTimeoutMillis = 120_000    // 120 sec
            connectTimeoutMillis = 60_000    // 1 min
        }

    }

    override suspend fun generateItinerary(prompt: String): ItineraryResponse {
        val apiKey = ApiKey // Use the ApiKey

        val request = GenerateContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt)
                    )
                )
            )
        )
        println("Prompt: $request")

        val response: GenerateContentResponse = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent") {
            headers {
                append("x-goog-api-key", apiKey)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

        return ItineraryResponse(response.candidates.first().content.parts.first().text)
    }
}