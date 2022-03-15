package com.example.gh_coursework.di

import com.example.gh_coursework.domain.usecase.point_details.AddPointDetailsUseCase
import com.example.gh_coursework.domain.usecase.point_details.AddPointDetailsUseCaseImpl
import com.example.gh_coursework.domain.usecase.point_details.GetPointDetailsUseCase
import com.example.gh_coursework.domain.usecase.point_details.GetPointDetailsUseCaseImpl
import com.example.gh_coursework.domain.usecase.point_preview.*
import com.example.gh_coursework.domain.usecase.point_tag.*
import com.example.gh_coursework.domain.usecase.route.*
import org.koin.dsl.module

val pointUseCasesModule = module {

    //Point preview
    single<AddPointPreviewUseCase> {
        AddPointPreviewUseCaseImpl(get())
    }

    single<GetPointsPreviewUseCase> {
        GetPointsPreviewUseCaseImpl(get())
    }

    single<DeletePointUseCase> {
        DeletePointUseCaseImpl(get())
    }

    //Point details
    single<AddPointDetailsUseCase> {
        AddPointDetailsUseCaseImpl(get())
    }

    single<GetPointDetailsUseCase> {
        GetPointDetailsUseCaseImpl(get())
    }

    //Point tag
    single<AddPointTagUseCase> {
        AddPointTagUseCaseImpl(get())
    }

    single<AddPointsTagsListUseCase> {
        AddPointsTagsListUseCaseImpl(get())
    }

    single<GetPointTagListUseCase> {
        GetPointTagListUseCaseImpl(get())
    }

    single<RemovePointsTagsListUseCase> {
        RemovePointsTagsListUseCaseImpl(get())
    }

    single<DeletePointTagUseCase> {
        DeletePointTagUseCaseImpl(get())
    }

    //Routes
    single<AddRouteUseCase> {
        AddRouteUseCaseImpl(get())
    }

    single<GetRoutesListUseCase> {
        GetRoutesListUseCaseImpl(get())
    }

    single<GetRoutePointsUseCase> {
        GetRoutePointsUseCaseImpl(get())
    }

    single<DeleteRouteUseCase> {
        DeleteRouteUseCaseImpl(get())
    }
}