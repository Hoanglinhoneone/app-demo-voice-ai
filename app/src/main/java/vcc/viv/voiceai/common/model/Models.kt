package vcc.viv.voiceai.common.model

import com.google.gson.annotations.SerializedName

data class Models (
    @SerializedName("object")
    val obj: String? = null,

    @SerializedName("data")
    val data: List<ModelInfo> = emptyList()
)

data class ModelInfo(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("object")
    val obj: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("publisher")
    val publisher: String? = null,

    @SerializedName("arch")
    val arch: String? = null,

    @SerializedName("compatibility_type")
    val compatType: String? = null,

    @SerializedName("quantization")
    val quantization: String? = null,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("max_context_length")
    val maxContextLen: Int = 0,
)