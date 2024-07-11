package com.crypto.recruitmenttest

import android.app.Application
import com.crypto.recruitmenttest.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DemoApplication)
            modules(applicationModule)
        }
    }
}
