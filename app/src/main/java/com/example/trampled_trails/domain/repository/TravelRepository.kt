package com.example.trampled_trails.domain.repository

import com.example.trampled_trails.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface TravelRepository {
    suspend fun addDeletedPoint(point: DeletedPointDomain)
    suspend fun addDeletedRoute(route: DeletedRouteDomain)
    suspend fun clearDeletedPointsTable()
    suspend fun clearDeletedRoutesTable()
    fun getDeletedPoints(): Flow<List<DeletedPointDomain>>
    fun getDeletedRoutes(): Flow<List<DeletedRouteDomain>>
    fun getImageListToDelete(): Flow<List<String>>
    suspend fun clearDeletedImagesTable()

    suspend fun addPointPreviewWithDetails(poi: PointPreviewDomain)
    suspend fun updatePointDetails(poi: PointDetailsDomain)
    suspend fun deleteAllPoints()
    suspend fun deletePoint(point: PointDetailsDomain)
    fun getAllPoints(): Flow<List<PointDomain>>

    suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>)
    suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>)

    suspend fun addPointImages(images: List<PointImageDomain>)
    suspend fun addRouteImages(images: List<RouteImageDomain>)
    suspend fun deletePointImage(image: PointImageDomain)
    suspend fun deleteRouteImage(image: RouteImageDomain)

    suspend fun addRoute(route: RouteDomain, pointsList: List<PointPreviewDomain>)
    suspend fun updateRoute(route: RouteDomain)
    suspend fun deleteAllRoutes()
    suspend fun deleteRoute(route: RouteDomain)

    suspend fun addRouteTagsList(routeTagsList: List<RouteTagsDomain>)
    suspend fun deleteTagsFromRoute(routeTagsList: List<RouteTagsDomain>)

    fun getAllPointsDetails(): Flow<List<PointDomain>>
    fun getPointsTagsList(pointId: String): Flow<List<PointTagDomain>>
    fun getPointDetails(id: String): Flow<PointDetailsDomain?>
    fun getPointTagList(): Flow<List<PointTagDomain>>

    fun getPointImages(pointId: String): Flow<List<PointImageDomain>>
    fun getRouteImages(routeId: String): Flow<List<RouteImageDomain>>

    fun getRoutesList(): Flow<List<RouteDomain>>
    fun getRouteDetails(routeId: String): Flow<RouteDomain>
    fun getRoutePointsList(routeId: String): Flow<List<PointDomain>>
    fun getRoutePointsImagesList(routeId: String): Flow<List<RoutePointsImagesDomain>>
    fun getRouteTags(): Flow<List<RouteTagDomain>>

    //Public
    suspend fun deleteRemotePoint(pointId: String)
    suspend fun deleteRemoteRoute(routeId: String)

    suspend fun uploadRouteToFirebase(route: RouteDomain, currentUser: String)
    suspend fun uploadPointsToFirebase(points: List<PointDomain>, currentUser: String)

    suspend fun saveFirebaseRouteToLocal(route: PublicRouteDomain)
    suspend fun saveFirebasePointsToLocal(points: List<PublicPointDomain>)

    suspend fun saveRouteImagesToFirebase(imageList: List<RouteImageDomain>, routeId: String)
    suspend fun savePointImagesToFirebase(imageList: List<PointImageDomain>, pointId: String)

    fun fetchRoutePoints(routeId: String): Flow<List<PublicPointDomain>>

    fun getUserFavouriteRoutes(userId: String): Flow<List<String>>
    suspend fun addRouteToFavourites(routeId: String, userId: String)
    suspend fun removeRouteFromFavourites(routeId: String, userId: String)

    fun getUserRoutes(userId: String): Flow<List<PublicRouteDomain>>
    fun getUserPoints(userId: String): Flow<List<PublicPointDomain>>

    suspend fun changeRouteAccess(routeId: String, isPublic: Boolean)
    fun deleteImagesFromFirebase(images: List<String>)
    suspend fun deleteAll()
}