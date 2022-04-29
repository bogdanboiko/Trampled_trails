package com.example.gh_coursework.di

import com.example.gh_coursework.domain.usecase.deleted.*
import com.example.gh_coursework.domain.usecase.image.*
import com.example.gh_coursework.domain.usecase.point_details.*
import com.example.gh_coursework.domain.usecase.point_preview.*
import com.example.gh_coursework.domain.usecase.point_tag.*
import com.example.gh_coursework.domain.usecase.public.*
import com.example.gh_coursework.domain.usecase.route_details.*
import com.example.gh_coursework.domain.usecase.route_points.GetRoutePointsListUseCase
import com.example.gh_coursework.domain.usecase.route_points.GetRoutePointsListUseCaseImpl
import com.example.gh_coursework.domain.usecase.route_preview.*
import com.example.gh_coursework.domain.usecase.route_tag.*
import org.koin.dsl.module

val pointUseCasesModule = module {

    //Deleted routes and points
    single<AddDeletedPointUseCase> {
        AddDeletedPointUseCaseImpl(get())
    }

    single<AddDeletedRouteUseCase> {
        AddDeletedRouteUseCaseImpl(get())
    }

    single<ClearDeletedPointsTableUseCase> {
        ClearDeletedPointsTableUseCaseImpl(get())
    }

    single<ClearDeletedRoutesTableUseCase> {
        ClearDeletedRoutesTableUseCaseImpl(get())
    }

    single<DeleteAllUseCase> {
        DeleteAllUseCaseImpl(get())
    }

    single<DeleteRemoteRouteUseCase> {
        DeleteRemoteRouteUseCaseImpl(get())
    }

    single<GetDeletedPointsUseCase> {
        GetDeletedPointsUseCaseImpl(get())
    }

    single<GetDeletedRoutesUseCase> {
        GetDeletedRoutesUseCaseImpl(get())
    }

    //Point preview
    single<AddPointPreviewWithDetailsUseCase> {
        AddPointPreviewWithDetailsUseCaseImpl(get())
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

    single<GetAllPointsDetails> {
        GetAllPointsDetailsImpl(get())
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

    single<FetchRoutePointsFromRemoteUseCase> {
        FetchRoutePointsFromRemoteUseCaseImpl(get())
    }

    single<SavePublicRouteToPrivateUseCase> {
        SavePublicRouteToPrivateUseCaseImpl(get())
    }

    single<GetPublicRouteListUseCase> {
        GetPublicRouteListUseCaseImpl(get())
    }

    single<GetUserRouteListUseCase> {
        GetUserRouteListUseCaseImpl(get())
    }

    single<MakePrivateRoutePublicUseCase> {
        MakePrivateRoutePublicUseCaseImpl(get())
    }
}