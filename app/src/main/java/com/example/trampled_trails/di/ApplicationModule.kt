package com.example.trampled_trails.di

import com.example.trampled_trails.MyApplication
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single {
        androidApplication() as MyApplication
    }
}