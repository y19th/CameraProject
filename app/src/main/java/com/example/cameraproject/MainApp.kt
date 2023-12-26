package com.example.cameraproject

import android.app.Application
import com.example.cameraproject.di.mainModule
import com.example.cameraproject.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MainApp)
            modules(listOf(
                mainModule,
                viewModelsModule
            ))
        }

    }
}