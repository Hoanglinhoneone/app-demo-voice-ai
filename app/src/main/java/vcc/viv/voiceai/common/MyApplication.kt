package vcc.viv.voiceai.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import vcc.viv.voiceai.BuildConfig

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

    }

    // example
    class ReleaseTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // handle in release mode
        }
    }
}