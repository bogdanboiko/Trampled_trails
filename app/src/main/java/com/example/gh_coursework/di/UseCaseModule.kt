package com.example.gh_coursework.di

import com.example.gh_coursework.domain.repository.TravelRepository
import com.example.gh_coursework.domain.usecase.AddPointPreviewUseCase
import com.example.gh_coursework.domain.usecase.AddPointPreviewUseCaseImpl
import com.example.gh_coursework.domain.usecase.GetPointsPreviewUseCase
import com.example.gh_coursework.domain.usecase.GetPointsPreviewUseCaseImpl
import org.koin.dsl.module

val pointUseCasesModule = module {
    single<AddPointPreviewUseCase> {
        AddPointPreviewUseCaseImpl(get())
    }

    single<GetPointsPreviewUseCase> {
        GetPointsPreviewUseCaseImpl(get())
    }
}