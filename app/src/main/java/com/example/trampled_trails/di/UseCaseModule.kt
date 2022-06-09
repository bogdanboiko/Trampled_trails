package com.example.trampled_trails.di

import com.example.trampled_trails.domain.usecase.deleted.*
import com.example.trampled_trails.domain.usecase.image.*
import com.example.trampled_trails.domain.usecase.main_activity.GetAllPointsUseCase
import com.example.trampled_trails.domain.usecase.main_activity.GetAllPointsUseCaseImpl
import com.example.trampled_trails.domain.usecase.point_details.*
import com.example.trampled_trails.domain.usecase.point_preview.*
import com.example.trampled_trails.domain.usecase.point_tag.*
import com.example.trampled_trails.domain.usecase.public.*
import com.example.trampled_trails.domain.usecase.route_details.*
import com.example.trampled_trails.domain.usecase.route_points.GetRoutePointsListUseCase
import com.example.trampled_trails.domain.usecase.route_points.GetRoutePointsListUseCaseImpl
import com.example.trampled_trails.domain.usecase.route_preview.*
import com.example.trampled_trails.domain.usecase.route_tag.*
import org.koin.dsl.module

val pointUseCasesModule = module {

    //Deleted routes and points
    single<ClearDeletedPointsTableUseCase> {
        ClearDeletedPointsTableUseCaseImpl(get())
    }

    single<ClearDeletedRoutesTableUseCase> {
        ClearDeletedRoutesTableUseCaseImpl(get())
    }

    single<DeleteAllUseCase> {
        DeleteAllUseCaseImpl(get())
    }

    single<ClearDeletedImagesTableUseCase> {
        ClearDeletedImagesTableUseCaseImpl(get())
    }

    single<GetDeletedImagesUseCase> {
        GetDeletedImagesUseCaseImpl(get())
    }

    single<DeleteRemotePointUseCase> {
        DeleteRemotePointUseCaseImpl(get())
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

    single<GetAllPointsUseCase> {
        GetAllPointsUseCaseImpl(get())
    }

    single<DeletePointUseCase> {
        DeletePointUseCaseImpl(get())
    }

    //Point details
    single<UpdatePointDetailsUseCase> {
        UpdatePointDetailsUseCaseImpl(get())
    }

    single<GetPointDetailsUseCase> {
        GetPointDetailsUseCaseImpl(get())
    }

    single<com.example.trampled_trails.domain.usecase.point_preview.GetAllPointsUseCase> {
        com.example.trampled_trails.domain.usecase.point_preview.GetAllPointsUseCaseImpl(get())
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

    //RoutePreview
    single<AddRouteUseCase> {
        AddRouteUseCaseImpl(get())
    }

    single<DeleteRouteUseCase> {
        DeleteRouteUseCaseImpl(get())
    }

    single<GetRoutesListUseCase> {
        GetRoutesListUseCaseImpl(get())
    }

    //RoutePoints
    single<GetRoutePointsListUseCase> {
        GetRoutePointsListUseCaseImpl(get())
    }

    //RouteDetails
    single<GetRouteDetailsUseCase> {
        GetRouteDetailsUseCaseImpl(get())
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
    single<AddRouteToFavouritesUseCase> {
        AddRouteToFavouritesUseCaseImpl(get())
    }

    single<FetchRoutePointsFromRemoteUseCase> {
        FetchRoutePointsFromRemoteUseCaseImpl(get())
    }

    single<GetUserFavouriteRoutesUseCase> {
        GetUserFavouriteRoutesUseCaseImpl(get())
    }

    single<GetUserPointListUseCase> {
        GetUserPointListUseCaseImpl(get())
    }

    single<GetUserRouteListUseCase> {
        GetUserRouteListUseCaseImpl(get())
    }

    single<ChangeRouteAccessUseCase> {
        ChangeRouteAccessUseCaseImpl(get())
    }

    single<RemoveRouteFromFavouritesUseCase> {
        RemoveRouteFromFavouritesUseCaseImpl(get())
    }

    single<SyncRemotePointsWithLocalUseCase> {
        SyncRemotePointsWithLocalUseCaseImpl(get())
    }

    single<SyncRemoteRoutesWithLocalUseCase> {
        SyncRemoteRoutesWithLocalUseCaseImpl(get())
    }

    single<UploadPointsToFirebaseUseCase> {
        UploadPointsToFirebaseUseCaseImpl(get())
    }

    single<UploadRouteToFirebaseUseCase> {
        UploadRouteToFirebaseUseCaseImpl(get())
    }

    single<DeleteImagesFromFirestoreUseCase> {
        DeleteImagesFromFirestoreUseCaseImpl(get())
    }
}