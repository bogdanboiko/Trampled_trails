package com.example.gh_coursework.domain.repository

import com.example.gh_coursework.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface TravelRepository {
    suspend fun addOrUpdatePointOfInterestDetails(poi: PointDetailsDomain)
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

    suspend fun addRoute(route: RouteDomain)
    suspend fun updateRoute(route: RouteDetailsDomain)
    suspend fun deleteRoute(route: RouteDomain)

    suspend fun addRouteTagsList(routeTagsList: List<RouteTagsDomain>)
    suspend fun deleteTagsFromRoute(routeTagsList: List<RouteTagsDomain>)

    fun getPointsTagsList(pointId: Long): Flow<List<PointTagDomain>>
    fun getPointOfInterestPreview(): Flow<List<PointPreviewDomain>>
    fun getPointOfInterestDetails(id: Long): Flow<PointDetailsDomain?>
    fun getPointTagList(): Flow<List<PointTagDomain>>

    fun getPointImages(pointId: Long): Flow<List<PointImageDomain>>
    fun getRouteImages(routeId: Long): Flow<List<RouteImageDomain>>

    fun getRoutesList(): Flow<List<RouteDomain>>
    fun getRouteDetails(routeId: Long): Flow<RouteDetailsDomain>

    fun getRouteTags(): Flow<List<RouteTagDomain>>
}