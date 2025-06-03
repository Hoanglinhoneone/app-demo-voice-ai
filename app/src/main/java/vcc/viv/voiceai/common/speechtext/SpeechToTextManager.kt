package vcc.viv.voiceai.common.speechtext

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

class SpeechToTextManager @Inject constructor(private val context: Context) : RecognitionListener {
    /* **********************************************************************
     * Variable
     ***********************************************************************/
    private val _spokenText = MutableStateFlow("")
    val spokenText: StateFlow<String> = _spokenText.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var language = ""
    private val recordAudioPermission = Manifest.permission.RECORD_AUDIO
    private val requestCode = 100

    val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    /* **********************************************************************
     * Function
     ***********************************************************************/
    fun startListening(language: String) {
        Timber.d("Start Recognizer to listening language : $language")
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Timber.d("Recognition is not available")
            _error.value = "Recognition is not available"
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)

        }
        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)
        Timber.i("Đang lắng nghe bạn nói!")
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(context)
            .setTitle("Quyền ghi âm bị từ chối")
            .setMessage("Vui lòng cấp quyền ghi âm trong Cài đặt để tiếp tục sử dụng tính năng này.")
            .setPositiveButton("Đi đến Cài đặt") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    fun stopListening() {
        Timber.i("Stop listening")
        _isListening.value = false
        recognizer.stopListening()
    }

    fun setLanguage(language: String) {
        this.language = language
    }

    fun shutdown() {
        recognizer.destroy()
        _isListening.value = false
        _error.value = null
        _spokenText.value = ""
    }

    /* **********************************************************************
     * Override
     ********************************************************************** */
    override fun onReadyForSpeech(params: Bundle?) {
        _error.value = null
    }

    override fun onBeginningOfSpeech() = Unit

    override fun onRmsChanged(rmsdB: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit

    override fun onEndOfSpeech() {
        _isListening.value = false
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) {
            return
        } else if (error == SpeechRecognizer.ERROR_NO_MATCH) {
            startListening(language)
        }
        _error.value = "ERROR: $error ${error.getSpeechToTextErrorMessage()}"
        Timber.d("Error: $error")
    }

    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let {
                Timber.d("Result: $it")
                _spokenText.value = it
            }
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit

    override fun onEvent(eventType: Int, params: Bundle?) = Unit

}