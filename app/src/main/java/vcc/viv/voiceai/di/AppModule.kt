package vcc.viv.voiceai.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vcc.viv.voiceai.data.network.ApiService
import vcc.viv.voiceai.data.repository.ChatRepository
import vcc.viv.voiceai.data.repository.ChatRepositoryImp
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
}