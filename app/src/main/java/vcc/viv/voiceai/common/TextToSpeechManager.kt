package vcc.viv.voiceai.common

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

class TextToSpeechManager @Inject constructor(private val context: Context) {

    /* **********************************************************************
     * Variable
     ********************************************************************** */
    private var textToSpeech: TextToSpeech? = null

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /* **********************************************************************
     * Init
     ********************************************************************** */
    init {
        initializeTextToSpeech()
    }

    /* **********************************************************************
     *  Private Function
     ********************************************************************** */
    // Khởi tạo TextToSpeech
    private fun initializeTextToSpeech() {
        Timber.i("Đang khởi tạo Text To Speech")
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                Timber.i("TextToSpeech initialized successfully")
                Timber.i("Tiến hành set up ngôn ngữ : ${Locale.getDefault()}")
                val result = textToSpeech?.setLanguage(Locale("vi", "VN"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Timber.d("Setup Language failure: $result")
                    _error.value = "Language not supported"
//                    installVoiceData()
                    _isInitialized.value = false
                } else {
                    Timber.i("Setup Language success")
                    _isInitialized.value = true
                    setupUtteranceListener()
                }
            } else {
                Timber.i("TextToSpeech initialization failed")
                _error.value = "Failed to initialize TextToSpeech"
                _isInitialized.value = false
            }
        }
    }

    //    private fun installVoiceData() {
//        Timber.i("Installing voice data")
//        val intent = Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
//        intent.addFlags(Settings.ACTION_TTS_SETTINGS)
//        context.startActivity(intent)
//    }
    // Theo dõi trạng thái khi nói
    private fun setupUtteranceListener() {
        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                Timber.i("Start speaking")
                _isSpeaking.value = true
            }

            override fun onDone(utteranceId: String?) {
                Timber.i("Done speaking")
                _isSpeaking.value = false
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                Timber.i(" Speech Error")
                _isSpeaking.value = false
                _error.value = "Error in error"
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?, errorCode: Int) {
                Timber.i("Speech Error : $utteranceId")
                _isSpeaking.value = false
                _error.value = "Error in error"
            }
        })
    }

    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        val tts = textToSpeech ?: run {
            Timber.i("Không thể nói")
            _error.value = "Text To Speech chưa được khởi tạo"
            return
        }
        Timber.d("Bắt đầu nói :$text")
        val utteranceId = UUID.randomUUID().toString()
        tts.speak(text, queueMode, null, utteranceId)
    }

    fun stop() {
        Timber.i("Dừng nói")
        textToSpeech?.stop()
        _isSpeaking.value = false
    }

    fun setVoice(voice: Voice) {
        Timber.i("Đã set voice : $voice")
        textToSpeech?.voice = voice
    }

    fun getSupportVoices(): Collection<Voice> {
        Timber.d("Đã get voices")
        return textToSpeech?.voices ?: emptyList()
    }


    fun setLanguage(locale: Locale): Boolean {
        val tts = textToSpeech ?: run {
            _error.value = "Text To Speech chưa được khởi tạo"
            return false
        }
        val result = tts.setLanguage(locale)
        return !(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
    }

    /** Điều chỉnh độ cao của giọng nói
     * @param pitch Giá trị từ 0.0 đến 2.0, với 1.0 là giá trị mặc định.
     * Nếu > 1.0f sẽ làm giọng nói cao hơn
     * Nếu < 1.0f sẽ làm giọng nói thấp hơn
     */
    fun setPitch(pitch: Float) {
        Timber.d("Đã set pitch = $pitch")
        textToSpeech?.setPitch(pitch)
    }

    /** Điều chỉnh tốc độ nói
     * @param rate Giá trị từ 0.0 đến 2.0, với 1.0 là giá trị mặc định.
     * Nếu > 1.0f sẽ làm nói nhanh hơn
     * Nếu < 1.0f sẽ làm giọng nói chậm đi
     */
    fun setSpeechRate(rate: Float) {
        Timber.d("Đã set SpeechRate: $rate")
        textToSpeech?.setSpeechRate(rate)
    }

    fun shutdown() {
        Timber.i("Shutting down TextToSpeech")
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        _isInitialized.value = false
        _isSpeaking.value = false
    }
}