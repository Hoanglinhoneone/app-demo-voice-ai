package vcc.viv.voiceai

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vcc.viv.voiceai.common.Constans
import vcc.viv.voiceai.common.SpeechToTextManager
import vcc.viv.voiceai.common.TextToSpeechManager
import vcc.viv.voiceai.common.base.BaseViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val textToSpeechManager: TextToSpeechManager,
    private val speechToTextManager: SpeechToTextManager
) : BaseViewModel() {
    /* **********************************************************************
     * Variable
     ***********************************************************************/
    private val _ttsState  = MutableStateFlow(TTSState())
    val ttsState: StateFlow<TTSState> = _ttsState.asStateFlow()

    private val _sttState = MutableStateFlow(STTState())
    val sttState: StateFlow<STTState> = _sttState.asStateFlow()

    private val _roleSpeak = MutableStateFlow("")
    val roleSpeak: StateFlow<String> = _roleSpeak.asStateFlow()

    /* **********************************************************************
     * Init
     ***********************************************************************/
    init {
//        TTS
//        viewModelScope.launch {
//            combine(
//                textToSpeechManager.isSpeaking,
//                textToSpeechManager.error,
//                textToSpeechManager.isInitialized,
//                speechToTextManager.isListening,
//                speechToTextManager.error,
//                speechToTextManager.spokenText
//            ) { isSpeaking, error, isInitialized, isListening, errorSTT, spokenText ->
//                _ttsState.value = _ttsState.value.copy(
//                    isSpeaking = isSpeaking,
//                    error = error,
//                    isInitialized = isInitialized
//                )
//                _sttState.value = _sttState.value.copy(
//                    isListening = isListening,
//                    error = errorSTT,
//                    spokenText = spokenText
//                )
//            }.collect()
//        }
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
                    _sttState.value = _sttState.value.copy(spokenText = spokenText)
                }
            }
        }
    }
    /* **********************************************************************
     * Function
     ***********************************************************************/
    fun updateRoleSpeak(role: String) {
        _roleSpeak.value = role
    }

    // TTS
    fun speak(text: String) {
        if (roleSpeak.value == Constans.Role.SYSTEM) {
            textToSpeechManager.speak(text)
        }
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

    fun setLanguage(locale: Locale) : Boolean {
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