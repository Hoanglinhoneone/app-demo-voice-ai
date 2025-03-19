package vcc.viv.voiceai.ui.screen.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import vcc.viv.voiceai.common.base.BaseViewModel
import vcc.viv.voiceai.common.model.Message
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(

) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(mutableListOf())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private fun updateMessages(message: Message) {
        val messages = _messages.value.toMutableList()
        messages.add(message)
        _messages.value = messages
    }

    fun updateMessageInput(input: String) {
        _uiState.value = _uiState.value.copy(messageInput = input)
        updateMessages(Message(input, true))
    }

    fun updateMessageOutput(output: String) {
        _uiState.value = _uiState.value.copy(messageOutput = output)
        updateMessages(Message(output, false))
    }

    fun sendMessage() {

    }
}

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val messageInput: String = "",
    val messageOutput: String = "",
)