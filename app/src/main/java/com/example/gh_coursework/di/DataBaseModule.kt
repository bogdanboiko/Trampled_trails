package com.example.gh_coursework.di

import androidx.room.Room
import com.example.gh_coursework.data.database.TravelDatabase
import org.koin.dsl.module

val localDataBaseModule = module {
    single {
        Room.databaseBuilder(get(), TravelDatabase::class.java, "Travel.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        get<TravelDatabase>().getPointPreviewDao()
    }

    single {
        get<TravelDatabase>().getRoutePreviewDao()
    }
}