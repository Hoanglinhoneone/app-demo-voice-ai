package vcc.viv.voiceai.common.model

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName("model")
    val model: String,

    @SerializedName("messages")
    val messages: List<Message>,

    @SerializedName("temperature")
    val temperature: Float,

    @SerializedName("max_tokens")
    val maxTokens: Int,

    @SerializedName("stream")
    val stream: Boolean
)
