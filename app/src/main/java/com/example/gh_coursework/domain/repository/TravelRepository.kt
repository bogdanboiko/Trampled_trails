package com.example.gh_coursework.domain.repository

import com.example.gh_coursework.domain.entity.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface TravelRepository {
    suspend fun updatePointOfInterestDetails(poi: PointDetailsDomain)
    suspend fun addPointOfInterestCoordinatesWithDetails(poi: PointPreviewDomain)
    suspend fun deletePoint(pointId: Long)

    suspend fun addPointTag(tag: PointTagDomain)
    suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>)
    suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>)
    suspend fun deletePointTag(tag: PointTagDomain)

    suspend fun addPointImages(images: List<PointImageDomain>)
    suspend fun addRouteImages(images: List<RouteImageDomain>)
    suspend fun deletePointImage(image: PointImageDomain)
    suspend fun deleteRouteImage(image: RouteImageDomain)

    suspend fun addRoute(route: RouteDomain, coordinatesList: List<RoutePointDomain>)
    suspend fun updateRoute(route: RouteDomain)
    suspend fun deleteRoute(route: RouteDomain)

    suspend fun addRouteTagsList(routeTagsList: List<RouteTagsDomain>)
    suspend fun deleteTagsFromRoute(routeTagsList: List<RouteTagsDomain>)

    fun getPointsTagsList(pointId: Long): Flow<List<PointTagDomain>>
    fun getPointOfInterestDetails(id: Long): Flow<PointDetailsDomain?>
    fun getPointTagList(): Flow<List<PointTagDomain>>

    fun getPointImages(pointId: Long): Flow<List<PointImageDomain>>
    fun getRouteImages(routeId: Long): Flow<List<RouteImageDomain>>

    fun getRoutesList(): Flow<List<RouteDomain>>
    fun getRouteDetails(routeId: Long): Flow<RouteDomain>
    fun getRoutePointsList(routeId: Long): Flow<List<RoutePointDomain>>
    fun getRoutePointsImagesList(routeId: Long): Flow<List<RoutePointsImagesDomain>>

    fun getRouteTags(): Flow<List<RouteTagDomain>>

    //Public
    fun publishRoute(route: RouteDomain, routePoints: List<RoutePointDomain>, currentUser: FirebaseUser)
    fun fetchRoutePoints(routeId: String): Flow<List<PublicRoutePointDomain>>
    fun getAllPointsDetails(): Flow<List<PointCompleteDetailsDomain>>
    suspend fun savePublicRouteToPrivate(route: PublicRouteDomain, points: List<PublicRoutePointDomain>)
}