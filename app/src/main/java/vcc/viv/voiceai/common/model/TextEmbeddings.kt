package vcc.viv.voiceai.common.model

import com.google.gson.annotations.SerializedName

data class TextEmbeddings(
    @SerializedName("object")
    val obj: String? = null,

    @SerializedName("data")
    val data: List<Data>? = null,

    @SerializedName("model")
    val model: String? = null,

    @SerializedName("usage")
    val usage: Usage? = null
)

data class Data(
    @SerializedName("object")
    val obj: String? = null,

    @SerializedName("data")
    val embedding: List<Double>? = null,

    @SerializedName("index")
    val index: Int? = null
)
