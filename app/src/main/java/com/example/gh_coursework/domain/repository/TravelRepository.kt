package com.example.gh_coursework.domain.repository

import com.example.gh_coursework.domain.entity.PointOfInterestDomain
import com.example.gh_coursework.domain.entity.PointOfInterestPreviewDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import kotlinx.coroutines.flow.Flow

interface TravelRepository {
    suspend fun addPointOfInterestDetails(poi: PointOfInterestDomain)
    suspend fun addPointOfInterestCoordinates(poi: PointOfInterestPreviewDomain)
    suspend fun addRoute(route: RouteDomain)
    suspend fun addRouteDetails(route: RoutePreviewDomain)
    fun getPointOfInterestPreview(id: Int): Flow<PointOfInterestPreviewDomain>
    fun getRoutePreview(routeId: Int): Flow<RoutePreviewDomain>
    fun getPointOfInterestDetails(id: Int): Flow<PointOfInterestDomain>
    fun getRouteDetails(routeId: Int): Flow<RouteDomain>
}