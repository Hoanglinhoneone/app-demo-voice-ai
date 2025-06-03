package vcc.viv.voiceai.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vcc.viv.voiceai.common.speechtext.SpeechToTextManager
import vcc.viv.voiceai.common.speechtext.TextToSpeechManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object VoiceModule {
    @Provides
    @Singleton
    fun provideSpeechToTextManager(@ApplicationContext context: Context): SpeechToTextManager {
        return SpeechToTextManager(context)
    }

    @Provides
    @Singleton
    fun provideTextToSpeechManager(@ApplicationContext context: Context): TextToSpeechManager {
        return TextToSpeechManager(context)
    }
}