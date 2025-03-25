package vcc.viv.voiceai.common.model

import com.google.gson.annotations.SerializedName

data class Models (
    @SerializedName("object")
    val obj: String? = null,

    @SerializedName("data")
    val data: List<ModelInfo> = emptyList()
)