package vcc.viv.voiceai

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import vcc.viv.voiceai.common.speechtext.TextToSpeechManager
import vcc.viv.voiceai.common.base.BaseViewModel
import vcc.viv.voiceai.common.llm.LargeLangModel
import vcc.viv.voiceai.common.model.ChatRequest
import vcc.viv.voiceai.common.model.Message
import vcc.viv.voiceai.common.model.ModelInfo
import vcc.viv.voiceai.common.model.Role
import vcc.viv.voiceai.common.speechtext.SpeechToTextManager
import vcc.viv.voiceai.data.repository.chat.ChatRepository
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class  MainViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val textToSpeechManager: TextToSpeechManager,
    private val speechToTextManager: SpeechToTextManager,
    private val largeLangModel: LargeLangModel
) : BaseViewModel() {
    /* **********************************************************************
     * Variable
     ***********************************************************************/
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // use to switch send message to server or gemini
    private val _isSendServer = MutableStateFlow(true)

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _ttsState = MutableStateFlow(TTSState())
    val ttsState: StateFlow<TTSState> = _ttsState.asStateFlow()

    private val _sttState = MutableStateFlow(STTState())
    val sttState: StateFlow<STTState> = _sttState.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(mutableListOf())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    /* **********************************************************************
     * Init
     ***********************************************************************/
    init {
        initModels()
        initModelInfo()
        viewModelScope.launch {
            launch {
                textToSpeechManager.isSpeaking.collect { isSpeaking ->
                    _ttsState.value = _ttsState.value.copy(isSpeaking = isSpeaking)
                    Timber.d("isSpeaking: $isSpeaking")
                    if (!isSpeaking) {
                        startListening()
                    }
                }
            }
            launch {
                textToSpeechManager.error.collect { error ->
                    if (!error.isNullOrEmpty()) {
                        _error.value = error
                    }
                }
            }

            launch {
                textToSpeechManager.isInitialized.collect { isInitialized ->
                    _ttsState.value = _ttsState.value.copy(isInitialized = isInitialized)
                }
            }
        }
        viewModelScope.launch {
            launch {
                speechToTextManager.isListening.collect { isListening ->
                    _sttState.value = _sttState.value.copy(isListening = isListening)
                }
            }
            launch {
                speechToTextManager.error.collect { error ->
                    if (!error.isNullOrEmpty()) {
                        _error.value = error
                    }
                }
            }
            launch {
                speechToTextManager.spokenText.collect { spokenText ->
                    spokenText.let {
                        _sttState.value = _sttState.value.copy(spokenText = spokenText)
                        if (it.isNotEmpty()) {
                            updateMessages(
                                Message(
                                    content = spokenText,
                                    participant = Role.USER.title
                                )
                            )
                            if (uiState.value.modelInfo?.id == "Gemini") sendMessageToGemini(
                                spokenText
                            )
                            else
                                sendMessageToServer(
                                    Message(
                                        content = spokenText,
                                        participant = Role.USER.title
                                    )
                                )
                        }
                    }
                }
            }
        }
    }

    /* **********************************************************************
     * Function
     ***********************************************************************/
    private fun initModels() {
        Timber.i("get list model")
        viewModelScope.launch {
            try {
                val result = chatRepository.getModels()
                _uiState.update { it.copy(models = result.data) }
                Timber.d("Models: $result")
            } catch (e: Exception) {
                Timber.e("Error: ${e.message}")
                _error.value = e.message
            }
        }
    }

    private fun initModelInfo(model: String = "deepseek-coder-v2-lite-instruct") {
        Timber.i("get model info")
        viewModelScope.launch {
            try {
                val result = chatRepository.getModelInfo(model)
                Timber.d("Model info: $result")
            } catch (e: Exception) {
                Timber.e("Error: ${e.message}")
                _error.value = e.message
            }
        }
    }

    fun changeModel(modelInfo: ModelInfo) {
        Timber.i("change model")
        _uiState.update { it.copy(modelInfo = modelInfo) }
        _messages.update { emptyList() }
    }

    private fun sendMessageToServer(message: Message) {
        Timber.i("Send message to server")
        viewModelScope.launch {
            try {
                Timber.d("LinhHN message: $message")
                val request = uiState.value.modelInfo?.let {
                    Timber.d("LinhHN model: ${it.id}")
                    ChatRequest(
                        it.id,
                        _messages.value,
                        0.7F,
                        -1,
                        false
                    )
                }
                Timber.d("LinhHN request: $request")
                val result = request?.let { chatRepository.postChatCompletion(it) }
                Timber.d("LinhHN result: $result")
                if (result != null) {
                    result.choices?.get(0)?.message?.let { speak(it.content) }
                    updateMessages(
                        Message(
                            content = result.choices?.get(0)?.message?.content ?: "",
                            participant = Role.ASSISTANT.title
                        )
                    )
                }
                Timber.d("Chat completion: $result")
            } catch (e: Exception) {
                Timber.e("Error: ${e.message}")
                _error.value = e.message
                _isSendServer.value = false
                _uiState.update {
                    it.copy(
                        modelInfo = ModelInfo(
                            id = "Gemini",
                            obj = "model",
                            type = "vlm",
                            publisher = "lmstudio-community",
                            arch = "gemma3",
                            compatType = "gguf",
                            quantization = "Q3_K_L",
                            state = "loaded",
                            maxContextLen = 131072
                        ),
                    )
                }
                sendMessageToGemini(message.content)
            }
        }
    }

    private fun updateMessages(message: Message) {
        Timber.i("Update messages: $message")
        val messages = _messages.value.toMutableList()
        messages.add(message)
        _messages.value = messages
    }

    private fun sendMessageToGemini(message: String) {
        Timber.i("Send message: $message")
        viewModelScope.launch {
            try {
                val respone = largeLangModel.sendMessage(message)
                respone?.let {
                    updateMessages(Message(content = it, participant = Role.ASSISTANT.title))
                    speak(respone)
                    Timber.d("Response: $it")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Timber.e("Error: ${e.message}")
            }
        }
    }

    // TTS
    private fun speak(text: String) {
        textToSpeechManager.speak(text)
    }

    fun stop() {
        textToSpeechManager.stop()
    }

    fun setPitch(pitch: Float) {
        textToSpeechManager.setPitch(pitch)
        _ttsState.update { it.copy(pitch = pitch) }
    }

    fun setSpeechRate(rate: Float) {
        textToSpeechManager.setSpeechRate(rate)
        _ttsState.update { it.copy(speechRate = rate) }
    }

    fun setLanguage(locale: Locale): Boolean {
        val isSupported = textToSpeechManager.setLanguage(locale)
        if (isSupported) {
            _ttsState.update { it.copy(currentLanguage = locale) }
        }
        return isSupported
    }

    fun clearError() {
        _ttsState.update { it.copy(error = null) }
    }

    // STT
    fun setLanguage(language: String) {
        speechToTextManager.setLanguage(language)
    }

    fun startListening() {
        speechToTextManager.startListening("vi-VN")
    }

    fun stopListening() {
        speechToTextManager.stopListening()
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeechManager.shutdown()
        speechToTextManager.shutdown()
    }

    /* **********************************************************************
     * Classes
     ***********************************************************************/
    data class UiState(
        val models: List<ModelInfo> = emptyList(),
        val modelInfo: ModelInfo? = ModelInfo(
            id = "gemma-3-27b-it",
            obj = "model",
            type = "vlm",
            publisher = "lmstudio-community",
            arch = "gemma3",
            compatType = "gguf",
            quantization = "Q3_K_L",
            state = "loaded",
            maxContextLen = 131072
        ),
    )

    data class TTSState(
        val isSpeaking: Boolean = false,
        val error: String? = null,
        val isInitialized: Boolean = false,
        val pitch: Float = 1.0f,
        val speechRate: Float = 1.0f,
        val currentLanguage: Locale = Locale.getDefault()
    )

    data class STTState(
        val isListening: Boolean = false,
        val error: String? = null,
        val currentLanguage: Locale = Locale.getDefault(),
        val spokenText: String = ""
    )
}
