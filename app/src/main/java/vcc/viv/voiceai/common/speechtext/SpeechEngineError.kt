package vcc.viv.voiceai.common.speechtext

import android.speech.SpeechRecognizer

fun Int.getSpeechToTextErrorMessage() = when (this) {
    SpeechRecognizer.ERROR_AUDIO -> "[$this] Audio recording error"
    SpeechRecognizer.ERROR_CANNOT_CHECK_SUPPORT -> "[$this] The service does not allow to check for support"
    SpeechRecognizer.ERROR_CANNOT_LISTEN_TO_DOWNLOAD_EVENTS -> "[$this] The service does not support listening to model downloads events"
    SpeechRecognizer.ERROR_CLIENT -> "[$this] Other client side errors"
    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "[$this] Insufficient permissions"
    SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED -> "[$this] Requested language is not available to be used with the current recognizer"
    SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> "[$this] Requested language is supported, but not available currently"
    SpeechRecognizer.ERROR_NETWORK -> "[$this] Other network related errors"
    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "[$this] Network operation timed out"
    SpeechRecognizer.ERROR_NO_MATCH -> "[$this] No recognition result matched"
    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "[$this] RecognitionService busy"
    SpeechRecognizer.ERROR_SERVER -> "[$this] Server sends error status"
    SpeechRecognizer.ERROR_SERVER_DISCONNECTED -> "[$this] Server has been disconnected"
    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "[$this] No speech input"
    SpeechRecognizer.ERROR_TOO_MANY_REQUESTS -> "[$this] Too many requests from the same client"
    else -> "[$this] Unknown error"
}