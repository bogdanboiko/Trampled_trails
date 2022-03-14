package com.example.gh_coursework.domain.repository

import com.example.gh_coursework.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface TravelRepository {
    suspend fun addOrUpdatePointOfInterestDetails(poi: PointDetailsDomain)
    suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain)
    suspend fun deletePoint(pointId: Int)
    suspend fun addRoute(route: RouteDomain)
    suspend fun addPointTag(tag: PointTagDomain)
    suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>)
    suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>)
    suspend fun deletePointTag(tag: PointTagDomain)
    fun getPointOfInterestPreview(): Flow<List<PointPreviewDomain>>
    fun getRoutePreview(routeId: Int): Flow<List<PointPreviewDomain>>
    fun getPointOfInterestDetails(id: Int): Flow<PointDetailsDomain?>
    fun getRoute(): Flow<RouteDomain>
    fun getRoutesList(): Flow<List<RouteDomain>>
    fun getPointTagList(): Flow<List<PointTagDomain>>
}