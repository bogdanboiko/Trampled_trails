package com.example.gh_coursework.di

import com.example.gh_coursework.domain.usecase.image.DeletePointImageUseCase
import com.example.gh_coursework.domain.usecase.image.DeletePointImageUseCaseImpl
import com.example.gh_coursework.domain.usecase.image.GetPointImagesUseCase
import com.example.gh_coursework.domain.usecase.image.GetPointImagesUseCaseImpl
import com.example.gh_coursework.domain.usecase.point_details.*
import com.example.gh_coursework.domain.usecase.point_preview.*
import com.example.gh_coursework.domain.usecase.point_tag.*
import com.example.gh_coursework.domain.usecase.route_details.GetRouteDetailsUseCase
import com.example.gh_coursework.domain.usecase.route_details.GetRouteDetailsUseCaseImpl
import com.example.gh_coursework.domain.usecase.route_details.UpdateRouteDetailsUseCase
import com.example.gh_coursework.domain.usecase.route_details.UpdateRouteDetailsUseCaseImpl
import com.example.gh_coursework.domain.usecase.route_preview.*
import com.example.gh_coursework.domain.usecase.route_tag.*
import org.koin.dsl.module

val pointUseCasesModule = module {

    //Point preview
    single<AddPointPreviewWithDetailsUseCase> {
        AddPointPreviewWithDetailsUseCaseImpl(get())
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

    //Point images
    single<AddPointImageListUseCase> {
        AddPointImageListUseCaseImpl(get())
    }

    single<DeletePointImageUseCase> {
        DeletePointImageUseCaseImpl(get())
    }

    single<GetPointImagesUseCase> {
        GetPointImagesUseCaseImpl(get())
    }

    //Point tag
    single<AddPointTagUseCase> {
        AddPointTagUseCaseImpl(get())
    }

    single<GetPointTagsUseCase> {
        GetPointTagsUseCaseImpl(get())
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

    //RoutePreview
    single<AddRouteUseCase> {
        AddRouteUseCaseImpl(get())
    }

    single<GetRoutesListUseCase> {
        GetRoutesListUseCaseImpl(get())
    }

    single<DeleteRouteUseCase> {
        DeleteRouteUseCaseImpl(get())
    }

    //RouteDetails
    single<GetRouteDetailsUseCase> {
        GetRouteDetailsUseCaseImpl(get())
    }

    single<UpdateRouteDetailsUseCase> {
        UpdateRouteDetailsUseCaseImpl(get())
    }

    //RouteTag
    single<AddRouteTagsUseCase> {
        AddRouteTagsUseCaseImpl(get())
    }

    single<GetRouteTagsUseCase> {
        GetRouteTagsUseCaseImpl(get())
    }

    single<DeleteTagsFromRouteUseCase> {
        DeleteTagsFromRouteUseCaseImpl(get())
    }
}