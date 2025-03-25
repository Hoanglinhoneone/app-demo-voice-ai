package vcc.viv.voiceai.common.model

import com.google.gson.annotations.SerializedName

enum class Role(val title: String) {
    USER("user"),
    ASSISTANT("system")
}

data class Message(
    @SerializedName("role")
    val participant: String = Role.USER.title,

    @SerializedName("content")
    val content: String = "",
)
