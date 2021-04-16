package io.hindbrain.todo

import android.app.Application
import timber.log.Timber


class TodoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
