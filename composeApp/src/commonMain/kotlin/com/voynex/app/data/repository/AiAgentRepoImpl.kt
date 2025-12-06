package com.voynex.app.data.repository

import com.voynex.app.data.remote.openai.Content
import com.voynex.app.data.remote.openai.GenerateContentResponse
import com.voynex.app.data.remote.openai.OpenAiApi
import com.voynex.app.domain.repository.AiAgentRepo

class AiAgentRepoImpl(private val openAiApi: OpenAiApi) : AiAgentRepo {

    override suspend fun callAiAgent(contents: List<Content>): GenerateContentResponse {
        return openAiApi.callAiAgent(contents)
    }
}