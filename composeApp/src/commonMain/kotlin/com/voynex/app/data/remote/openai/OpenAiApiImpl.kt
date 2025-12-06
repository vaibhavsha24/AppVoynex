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
    val apiKey = ApiKey // Use the ApiKey

    val baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent"
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

        val request = GenerateContentRequest(
            contents = listOf(
                Content(
                    role = "User",
                    parts = listOf(
                        Part(text = prompt)
                    )
                )
            )
        )
        println("Prompt: $request")

        val response: GenerateContentResponse = client.post(baseUrl) {
            headers {
                append("x-goog-api-key", apiKey)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

        return ItineraryResponse(response.candidates.first().content.parts.first().text)
    }

    override suspend fun callAiAgent(contents: List<Content>): GenerateContentResponse {
        val request = GenerateContentRequest(contents = contents)
        return client.post(baseUrl) {
            headers {
                append("x-goog-api-key", apiKey)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

}