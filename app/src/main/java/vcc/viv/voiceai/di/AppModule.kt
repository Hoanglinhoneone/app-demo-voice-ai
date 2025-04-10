package vcc.viv.voiceai.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vcc.viv.voiceai.data.datasource.database.JsonDataSource
import vcc.viv.voiceai.data.datasource.network.ApiService
import vcc.viv.voiceai.data.repository.chat.ChatRepository
import vcc.viv.voiceai.data.repository.chat.ChatRepositoryImp
import vcc.viv.voiceai.data.repository.product.ProductRepository
import vcc.viv.voiceai.data.repository.product.ProductRepositoryImp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun bindChatRepository(apiService: ApiService): ChatRepository {
        return ChatRepositoryImp(apiService)
    }

    @Provides
    @Singleton
    fun provideProductRepository(jsonDataSource: JsonDataSource) : ProductRepository {
        return ProductRepositoryImp(jsonDataSource)
    }
}