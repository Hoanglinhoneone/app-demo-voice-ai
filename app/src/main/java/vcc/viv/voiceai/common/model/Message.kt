package vcc.viv.voiceai.common.model

import com.google.gson.annotations.SerializedName

enum class Role(val title: String) {
    USER("user"),
    ASSISTANT("assistant"),
}

enum class Participant(val title: String) {
    USER("user \uD83D\uDDE3\uFE0F "),
    ASSISTANT("\uD83E\uDD16 system"),
    ERROR("error")
}

data class Message(
    @SerializedName("role")
    val participant: String = Role.USER.title,

    @SerializedName("content")
    val content: String = "",
)
