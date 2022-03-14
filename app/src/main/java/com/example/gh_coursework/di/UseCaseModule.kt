package com.example.gh_coursework.di

import com.example.gh_coursework.domain.usecase.*
import org.koin.dsl.module

val pointUseCasesModule = module {
    single<AddPointDetailsUseCase> {
        AddPointDetailsUseCaseImpl(get())
    }

    single<AddPointPreviewUseCase> {
        AddPointPreviewUseCaseImpl(get())
    }

    single<AddRouteUseCase> {
        AddRouteUseCaseImpl(get())
    }

    single<DeletePointUseCase> {
        DeletePointUseCaseImpl(get())
    }

    single<GetPointDetailsUseCase> {
        GetPointDetailsUseCaseImpl(get())
    }

    single<GetPointsPreviewUseCase> {
        GetPointsPreviewUseCaseImpl(get())
    }

    single<GetRoutesListUseCase> {
        GetRoutesListUseCaseImpl(get())
    }

    single<GetRouteUseCase> {
        GetRouteUseCaseImpl(get())
    }

    single<AddPointTagUseCase> {
        AddPointTagUseCaseImpl(get())
    }

    single<GetPointTagListUseCase> {
        GetPointTagListUseCaseImpl(get())
    }

    single<AddPointsTagsListUseCase> {
        AddPointsTagsListUseCaseImpl(get())
    }

    single<RemovePointsTagsListUseCase> {
        RemovePointsTagsListUseCaseImpl(get())
    }

    single<DeletePointTagUseCase> {
        DeletePointTagUseCaseImpl(get())
    }
}