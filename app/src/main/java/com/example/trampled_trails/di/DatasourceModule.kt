package com.example.trampled_trails.di

import com.example.trampled_trails.data.TravelRepositoryImpl
import com.example.trampled_trails.data.database.LocalDataSrcIml
import com.example.trampled_trails.data.datasource.TravelDatasource
import com.example.trampled_trails.data.remote.RemoteDataSrcImpl
import com.example.trampled_trails.domain.repository.TravelRepository
import org.koin.dsl.module

val datasourceModule = module {
    single<TravelDatasource.Remote> {
        RemoteDataSrcImpl(get(), get())
    }

    single<TravelDatasource.Local> {
        LocalDataSrcIml(get(), get(), get(), get(), get(), get())
    }
}

val repositoryModule = module {
    single<TravelRepository> {
        TravelRepositoryImpl(get(), get())
    }
}