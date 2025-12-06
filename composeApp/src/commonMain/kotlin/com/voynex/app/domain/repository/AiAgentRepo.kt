package com.voynex.app.domain.repository

import com.voynex.app.data.remote.openai.Content
import com.voynex.app.data.remote.openai.GenerateContentResponse

interface AiAgentRepo {
    suspend fun callAiAgent(contents: List<Content>): GenerateContentResponse
}