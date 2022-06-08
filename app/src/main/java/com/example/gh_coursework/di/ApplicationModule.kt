package com.example.gh_coursework.di

import com.example.gh_coursework.MyApplication
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single {
        androidApplication() as MyApplication
    }
}