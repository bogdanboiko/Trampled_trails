package com.example.gh_coursework

import android.app.Application
import com.example.gh_coursework.di.ModuleList
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