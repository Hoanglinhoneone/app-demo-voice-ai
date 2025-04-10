package vcc.viv.voiceai.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vcc.viv.voiceai.data.datasource.database.JsonDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideJsonDataSource(@ApplicationContext context: Context): JsonDataSource {
        return JsonDataSource(context)
    }
}