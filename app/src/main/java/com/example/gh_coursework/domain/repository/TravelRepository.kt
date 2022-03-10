package com.example.gh_coursework.domain.repository

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointPreviewDomain
import kotlinx.coroutines.flow.Flow

interface TravelRepository {
    suspend fun addOrUpdatePointOfInterestDetails(poi: PointDetailsDomain)
    suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain)
    suspend fun deletePoint(pointId: Int)
    suspend fun addRoute(route: RouteDomain)
    suspend fun addRouteDetails(route: RouteDomain)
    fun getPointOfInterestPreview(): Flow<List<PointPreviewDomain>>
    fun getRoutePreview(routeId: Int): Flow<List<RoutePointPreviewDomain>>
    fun getPointOfInterestDetails(id: Int): Flow<PointDetailsDomain?>
    fun getRouteDetails(routeId: Int): Flow<RouteDomain>
}