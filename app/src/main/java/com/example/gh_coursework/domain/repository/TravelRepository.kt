package com.example.gh_coursework.domain.repository

import com.example.gh_coursework.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface TravelRepository {
    suspend fun addDeletedPoint(point: DeletedPointDomain)
    suspend fun addDeletedRoute(route: DeletedRouteDomain)
    suspend fun clearDeletedPointsTable()
    suspend fun clearDeletedRoutesTable()
    fun getDeletedPoints(): Flow<List<DeletedPointDomain>>
    fun getDeletedRoutes(): Flow<List<DeletedRouteDomain>>

    suspend fun addPointCoordinatesWithDetails(poi: PointPreviewDomain)
    suspend fun updatePointDetails(poi: PointDetailsDomain)
    suspend fun deleteAllPoints()
    suspend fun deletePoint(pointId: String)
    fun getAllPoints(): Flow<List<PointDomain>>

    suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>)
    suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>)

    suspend fun addPointImages(images: List<PointImageDomain>)
    suspend fun addRouteImages(images: List<RouteImageDomain>)
    suspend fun deletePointImage(image: PointImageDomain)
    suspend fun deleteRouteImage(image: RouteImageDomain)

    suspend fun addRoute(route: RouteDomain, coordinatesList: List<PointDomain>)
    suspend fun updateRoute(route: RouteDomain)
    suspend fun deleteAllRoutes()
    suspend fun deleteRoute(route: RouteDomain)

    suspend fun addRouteTagsList(routeTagsList: List<RouteTagsDomain>)
    suspend fun deleteTagsFromRoute(routeTagsList: List<RouteTagsDomain>)

    fun getAllPointsDetails(): Flow<List<PointDomain>>
    fun getPointsTagsList(pointId: String): Flow<List<PointTagDomain>>
    fun getPointOfInterestDetails(id: String): Flow<PointDetailsDomain?>
    fun getPointTagList(): Flow<List<PointTagDomain>>

    fun getPointImages(pointId: String): Flow<List<PointImageDomain>>
    fun getRouteImages(routeId: String): Flow<List<RouteImageDomain>>

    fun getRoutesList(): Flow<List<RouteDomain>>
    fun getPublicRoutesList(): Flow<List<PublicRouteDomain>>
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

    fun getUserRoutes(userId: String): Flow<List<PublicRouteDomain>>
    fun getUserPoints(userId: String): Flow<List<PublicPointDomain>>

    suspend fun makePrivateRoutePublic(routeId: String)
    suspend fun makePublicRoutePrivate(routeId: String)
}