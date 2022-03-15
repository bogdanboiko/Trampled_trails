package com.example.gh_coursework.domain.repository

import com.example.gh_coursework.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface TravelRepository {
    suspend fun addOrUpdatePointOfInterestDetails(poi: PointDetailsDomain)
    suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain)
    suspend fun deletePoint(pointId: Int)

    suspend fun addPointTag(tag: PointTagDomain)
    suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>)
    suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>)
    suspend fun deletePointTag(tag: PointTagDomain)

    suspend fun addRoute(route: RouteDomain)
    suspend fun deleteRoute(routeId: Int)
    suspend fun deleteRoutePoints(routeId: Int)

    fun getPointOfInterestPreview(): Flow<List<PointPreviewDomain>>
    fun getPointOfInterestDetails(id: Int): Flow<PointDetailsDomain?>
    fun getPointTagList(): Flow<List<PointTagDomain>>

    fun getRoutesList(): Flow<List<RouteDomain>>
    fun getRoutePoints(routeId: Int): Flow<List<RoutePointDomain>>
}