package com.example.trampled_trails

import android.app.Application
import com.example.trampled_trails.di.ModuleList
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(ModuleList)
        }
    }
}