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

    suspend fun updatePointOfInterestDetails(poi: PointDetailsDomain)
    suspend fun addPointOfInterestCoordinatesWithDetails(poi: PointPreviewDomain)
    suspend fun deleteAllPoints()
    suspend fun deletePoint(pointId: String)

    suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>)
    suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>)

    suspend fun addPointImages(images: List<PointImageDomain>)
    suspend fun addRouteImages(images: List<RouteImageDomain>)
    suspend fun deletePointImage(image: PointImageDomain)
    suspend fun deleteRouteImage(image: RouteImageDomain)

    suspend fun addRoute(route: RouteDomain, coordinatesList: List<RoutePointDomain>)
    suspend fun updateRoute(route: RouteDomain)
    suspend fun deleteAllRoutes()
    suspend fun deleteRoute(route: RouteDomain)

    suspend fun addRouteTagsList(routeTagsList: List<RouteTagsDomain>)
    suspend fun deleteTagsFromRoute(routeTagsList: List<RouteTagsDomain>)

    fun getPointsTagsList(pointId: String): Flow<List<PointTagDomain>>
    fun getPointOfInterestDetails(id: String): Flow<PointDetailsDomain?>
    fun getPointTagList(): Flow<List<PointTagDomain>>

    fun getPointImages(pointId: String): Flow<List<PointImageDomain>>
    fun getRouteImages(routeId: String): Flow<List<RouteImageDomain>>

    fun getRoutesList(): Flow<List<RouteDomain>>
    fun getPublicRoutesList(): Flow<List<RouteDomain>>
    fun getRouteDetails(routeId: String): Flow<RouteDomain>
    fun getRoutePointsList(routeId: String): Flow<List<RoutePointDomain>>
    fun getRoutePointsImagesList(routeId: String): Flow<List<RoutePointsImagesDomain>>
    fun getRouteTags(): Flow<List<RouteTagDomain>>

    //Public
    suspend fun deleteRemotePoint(pointId: String)
    suspend fun deleteRemoteRoute(routeId: String)

    suspend fun publishRoute(route: RouteDomain, routePoints: List<RoutePointDomain>, currentUser: String)

    suspend fun saveFirebaseRouteToLocal(route: PublicRouteDomain, points: List<PublicRoutePointDomain>)
    suspend fun saveRouteImagesToFirebase(imageList: List<RouteImageDomain>, routeId: String)
    suspend fun savePointImagesToFirebase(imageList: List<PointImageDomain>, pointId: String, routeId: String)

    fun fetchRoutePoints(routeId: String): Flow<List<PublicRoutePointDomain>>
    fun getAllPointsDetails(): Flow<List<PointCompleteDetailsDomain>>
    fun getUserRoutes(userId: String): Flow<List<PublicRouteDomain>>
    fun makePrivateRoutePublic(routeId: String)
}