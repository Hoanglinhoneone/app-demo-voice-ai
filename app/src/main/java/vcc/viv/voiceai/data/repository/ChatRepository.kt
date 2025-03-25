package vcc.viv.voiceai.data.repository

import vcc.viv.voiceai.common.model.ChatCompletion
import vcc.viv.voiceai.common.model.ChatRequest
import vcc.viv.voiceai.common.model.ModelInfo
import vcc.viv.voiceai.common.model.Models
import vcc.viv.voiceai.common.model.TextEmbeddings

interface ChatRepository {

    suspend fun getModels(): Models

    suspend fun getModelInfo(model: String): ModelInfo

    suspend fun postChatCompletion(request: ChatRequest): ChatCompletion
//
//    suspend fun postTextCompletion(): ChatCompletion
//
//    suspend fun postTextEmbedding(): TextEmbeddings
}
