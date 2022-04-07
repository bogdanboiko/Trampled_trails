package com.example.gh_coursework.di

import com.example.gh_coursework.domain.usecase.image.*
import com.example.gh_coursework.domain.usecase.point_details.*
import com.example.gh_coursework.domain.usecase.point_preview.*
import com.example.gh_coursework.domain.usecase.point_tag.*
import com.example.gh_coursework.domain.usecase.public.PublishRouteUseCase
import com.example.gh_coursework.domain.usecase.public.PublishRouteUseCaseImpl
import com.example.gh_coursework.domain.usecase.route_details.*
import com.example.gh_coursework.domain.usecase.route_points.GetRoutePointsListUseCase
import com.example.gh_coursework.domain.usecase.route_points.GetRoutePointsListUseCaseImpl
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

    single<GetRoutePointsListUseCase> {
        GetRoutePointsListUseCaseImpl(get())
    }

    single<GetRoutePointsImagesUseCase> {
        GetRoutePointsImagesUseCaseImpl(get())
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

    //RouteImages
    single<AddRouteImageListUseCase> {
        AddRouteImageListUseCaseImpl(get())
    }

    single<DeleteRouteImageUseCase> {
        DeleteRouteImageUseCaseImpl(get())
    }

    single<GetRouteImagesUseCase> {
        GetRouteImagesUseCaseImpl(get())
    }

    //Public
    single<PublishRouteUseCase> {
        PublishRouteUseCaseImpl(get())
    }
}