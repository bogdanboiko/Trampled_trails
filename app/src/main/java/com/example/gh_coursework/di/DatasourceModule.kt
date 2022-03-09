package com.example.gh_coursework.di

import com.example.gh_coursework.data.TravelRepositoryImpl
import com.example.gh_coursework.data.database.LocalDataSrcIml
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.domain.repository.TravelRepository
import org.koin.dsl.module

val datasourceModule = module {
   // TODO("Remote datasource impl")

    single<TravelDatasource.Local> {
        LocalDataSrcIml(get(), get())
    }
}

val repositoryModule = module {
    single<TravelRepository> {
        TravelRepositoryImpl(get())
    }
}