package vcc.viv.voiceai.common.llm

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vcc.viv.voiceai.BuildConfig
import javax.inject.Inject

class LargeLangModel @Inject constructor() {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = BuildConfig.API_KEY,
    )
    private val chatSession = generativeModel.startChat()

    suspend fun sendMessage(message: String): String? = withContext(Dispatchers.IO) {
        val respone = chatSession.sendMessage(message)
        return@withContext respone.text
    }
}
