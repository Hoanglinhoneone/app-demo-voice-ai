package vcc.viv.voiceai

import androidx.collection.MutableScatterSet
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import vcc.viv.voiceai.common.TextToSpeechManager
import vcc.viv.voiceai.common.base.BaseViewModel
import vcc.viv.voiceai.common.llm.LargeLangModel
import vcc.viv.voiceai.common.model.ChatCompletion
import vcc.viv.voiceai.common.model.ChatRequest
import vcc.viv.voiceai.common.model.Message
import vcc.viv.voiceai.common.model.Role
import vcc.viv.voiceai.common.speech.SpeechToTextManager
import vcc.viv.voiceai.data.repository.ChatRepository
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val textToSpeechManager: TextToSpeechManager,
    private val speechToTextManager: SpeechToTextManager,
    private val largeLangModel: LargeLangModel
) : BaseViewModel() {
    /* **********************************************************************
     * Variable
     ***********************************************************************/
    private val _ttsState = MutableStateFlow(TTSState())
    val ttsState: StateFlow<TTSState> = _ttsState.asStateFlow()

    private val _sttState = MutableStateFlow(STTState())
    val sttState: StateFlow<STTState> = _sttState.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(mutableListOf())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _messageResponse = MutableStateFlow("")

    //    private val _models = MutableStateFlow(ModelState())
//    val models: StateFlow<ModelState> = _models.asStateFlow()
    /* **********************************************************************
     * Init
     ***********************************************************************/
    init {
        getModels()
        getModelInfo()
        // TTS
        viewModelScope.launch {
            launch {
                textToSpeechManager.isSpeaking.collect { isSpeaking ->
                    _ttsState.value = _ttsState.value.copy(isSpeaking = isSpeaking)
                }
            }
            launch {
                textToSpeechManager.error.collect { error ->
                    _ttsState.value = _ttsState.value.copy(error = error)
                }
            }

            launch {
                textToSpeechManager.isInitialized.collect { isInitialized ->
                    _ttsState.value = _ttsState.value.copy(isInitialized = isInitialized)
                }
            }
        }
//        STT
        viewModelScope.launch {
            launch {
                speechToTextManager.isListening.collect { isListening ->
                    _sttState.value = _sttState.value.copy(isListening = isListening)
                }
            }
            launch {
                speechToTextManager.error.collect { error ->
                    _sttState.value = _sttState.value.copy(error = error)
                }
            }
            launch {
                speechToTextManager.spokenText.collect { spokenText ->
                    spokenText.let {
                        _sttState.value = _sttState.value.copy(spokenText = spokenText)
                        if (it.isNotEmpty()) {
//                            sendMessage(spokenText)
                            sendMessageToServer(Message(content = spokenText, participant = Role.USER.title))
//                            updateMessages(Message(content = spokenText, participant = Role.USER.title))
                        }
                    }
                }
            }
        }
    }

    /* **********************************************************************
     * Function
     ***********************************************************************/
    private fun getModels() {
        viewModelScope.launch {
            try {
                val result = chatRepository.getModels()
                Timber.d("Models: $result")
            } catch (e: Exception) {
                Timber.e("Error: ${e.message}")
            }
        }
    }

    private fun getModelInfo(model: String = "gemma-3-27b-it") {
        viewModelScope.launch {
            try {
                val result = chatRepository.getModelInfo(model)
                Timber.d("Model info: $result")
            } catch (e: Exception) {
                Timber.e("Error: ${e.message}")
            }
        }
    }

    private fun sendMessageToServer(message: Message){
        _messages.update { it + message }
        viewModelScope.launch {
            try {
                val request = ChatRequest(
                    "qwen2-vl-7b-instruct",
                    _messages.value,
                    0.7F,
                    -1,
                    false
                    )
                val result = chatRepository.postChatCompletion(request)
                updateMessages(Message(content = result.choices?.get(0)?.message?.content ?: "", participant = Role.ASSISTANT.title))
                Timber.d("Chat completion: $result")
            } catch (e: Exception) {
                Timber.e("Error: ${e.message}")
            }
        }
    }

    private fun updateMessages(message: Message) {
        val messages = _messages.value.toMutableList()
        messages.add(message)
        _messages.value = messages
    }

    fun sendMessage(message: String) {
        Timber.i("Send message: $message")
//        viewModelScope.launch {
//            try {
//                val respone = largeLangModel.sendMessage(message)
//                respone?.let {
//                    updateMessages(Message(content = it, participant = Role.ASSISTANT.title))
//                    _messageResponse.value = it
//                    speak(result)
//                    Timber.d("Response: $it")
//                }
//            } catch (e: Exception) {
//                Timber.e("Error: ${e.message}")
//            }
//        }
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

    // shutdown viewmodel
    override fun onCleared() {
        super.onCleared()
        textToSpeechManager.shutdown()
        speechToTextManager.shutdown()
    }

    /* **********************************************************************
     * Classes
     ***********************************************************************/
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