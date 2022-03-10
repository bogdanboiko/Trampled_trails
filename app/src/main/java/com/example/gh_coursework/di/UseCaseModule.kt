package com.example.gh_coursework.di

import com.example.gh_coursework.domain.usecase.*
import org.koin.dsl.module

val pointUseCasesModule = module {
    single<AddPointPreviewUseCase> {
        AddPointPreviewUseCaseImpl(get())
    }

    single<GetPointsPreviewUseCase> {
        GetPointsPreviewUseCaseImpl(get())
    }

    single<GetPointDetailsUseCase> {
        GetPointDetailsUseCaseImpl(get())
    }

    single<AddPointDetailsUseCase> {
        AddPointDetailsUseCaseImpl(get())
    }

    single<DeletePointUseCase> {
        DeletePointUseCaseImpl(get())
    }
}