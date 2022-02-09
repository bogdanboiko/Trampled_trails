package com.example.gh_coursework.di

import com.example.gh_coursework.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        MoshiConverterFactory.create(get())
    }

    TODO("Interceptor, okhttpclient")
}

val retrofitModule = module {
    single {
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(get<MoshiConverterFactory>()).client(get()).build()
    }
}

val apiModule = module {
    TODO("Service build")
}