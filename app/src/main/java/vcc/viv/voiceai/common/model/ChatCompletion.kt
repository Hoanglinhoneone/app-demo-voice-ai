package vcc.viv.voiceai.common.model

import com.google.gson.annotations.SerializedName

data class ChatCompletion(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("object")
    val obj: String? = null,

    @SerializedName("created")
    val created: Long? = null,

    @SerializedName("model")
    val model: String? = null,

    @SerializedName("choices")
    val choices: List<Choice>? = null,

    @SerializedName("usage")
    val usage: Usage? = null,

    @SerializedName("stats")
    val stats: Stats? = null,

    @SerializedName("model_info")
    val modelInfo: ModelInfoMini? = null,

    @SerializedName("runtime")
    val runtime: Runtime? = null
)

data class Choice(
    @SerializedName("index")
    val index: Int = 0,

    @SerializedName("text")
    val text: String? = null,

    @SerializedName("logprobs")
    val logProBs: Any? = null,

    @SerializedName("finish_reason")
    val finishReason: String? = null,

    @SerializedName("message")
    val message: Message? = null,
)

data class Usage(
    @SerializedName("prompt_tokens")
    val promptTokens: Int = 0,

    @SerializedName("completion_tokens")
    val completionTokens: Int? = null,

    @SerializedName("total_tokens")
    val totalTokens: Int? = null,
)


data class Stats(
    @SerializedName("tokens_per_second")
    val tokensPerSecond: Double? = null,

    @SerializedName("time_to_first_token")
    val timeToFirstToken: Double? = null,

    @SerializedName("generation_time")
    val generationTime: Double? = null,

    @SerializedName("stop_reason")
    val stopReason: String? = null,
)

data class ModelInfoMini(
    @SerializedName("arch")
    val arch: String = "",

    @SerializedName("quant")
    val quant: String? = null,

    @SerializedName("format")
    val format: String? = null,

    @SerializedName("context_length")
    val contextLength: Int? = null,
)

data class Runtime(
    @SerializedName("name")
    val name: String = "",

    @SerializedName("version")
    val version: String? = null,

    @SerializedName("supported_formats")
    val supportedFormats: List<String>? = null,
)
