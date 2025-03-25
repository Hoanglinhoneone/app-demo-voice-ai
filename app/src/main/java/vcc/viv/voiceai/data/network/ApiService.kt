package vcc.viv.voiceai.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import vcc.viv.voiceai.common.model.ChatCompletion
import vcc.viv.voiceai.common.model.ChatRequest
import vcc.viv.voiceai.common.model.ModelInfo
import vcc.viv.voiceai.common.model.Models
import vcc.viv.voiceai.common.model.TextEmbeddings

interface ApiService {

    @GET("/api/v0/models")
    suspend fun getModels(): Models

    @GET("/api/v0/models/{model}")
    suspend fun getModelInfo(@Path("model") model: String): ModelInfo

    @Headers("Content-Type: application/json")
    @POST("/api/v0/chat/completions")
    suspend fun postChatCompletion(@Body request: ChatRequest): ChatCompletion

    @POST("/api/v0/completions ")
    suspend fun postTextCompletion(): ChatCompletion

    @POST("/api/v0/embeddings")
    suspend fun postTextEmbedding(): TextEmbeddings
}