package vcc.viv.voiceai.data.repository.chat

import vcc.viv.voiceai.common.model.ChatCompletion
import vcc.viv.voiceai.common.model.ChatRequest
import vcc.viv.voiceai.common.model.ModelInfo
import vcc.viv.voiceai.common.model.Models
import vcc.viv.voiceai.data.datasource.network.ApiService
import javax.inject.Inject

class ChatRepositoryImp @Inject constructor(
    private val apiService: ApiService
) : ChatRepository {

    override suspend fun getModels(): Models = apiService.getModels()

    override suspend fun getModelInfo(model: String): ModelInfo = apiService.getModelInfo(model)

    override suspend fun postChatCompletion(request: ChatRequest): ChatCompletion {
        return apiService.postChatCompletion(request)
    }
//
//    override suspend fun postTextCompletion(): ChatCompletion {
//
//    }
//
//    override suspend fun postTextEmbedding(): TextEmbeddings {
//
//    }
}