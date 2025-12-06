package com.voynex.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voynex.app.data.remote.openai.Content
import com.voynex.app.data.remote.openai.OpenAiApi
import com.voynex.app.data.remote.openai.Part
import com.voynex.app.domain.model.ChatMessage
import com.voynex.app.domain.repository.AiAgentRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false
)

class ChatViewModel(private val aiAgentRepo: AiAgentRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    private val conversationHistory = mutableListOf<Content>()

    @OptIn(ExperimentalUuidApi::class)
    fun sendMessage(text: String) {
        viewModelScope.launch {
            // Add user message to the chat and history
            val userMessage = ChatMessage(id = Uuid.random().toString(), text = text, isMine = true)
            conversationHistory.add(Content(role = "user", parts = listOf(Part(text = text))))
            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + userMessage,
                isLoading = true
            )

            try {
                // Get AI response from the API
                val response = aiAgentRepo.callAiAgent(conversationHistory)
                val aiResponse = response.candidates.first().content.parts.first().text
                val aiMessage = ChatMessage(id = Uuid.random().toString(), text = aiResponse, isMine = false)
                conversationHistory.add(Content(role = "model", parts = listOf(Part(text = aiResponse))))

                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + aiMessage,
                    isLoading = false
                )
            } catch (e: Exception) {
                // Handle error
                val errorMessage = ChatMessage(
                    id = Uuid.random().toString(),
                    text = "Error: ${e.message}",
                    isMine = false
                )
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + errorMessage,
                    isLoading = false
                )
            }
        }
    }
}