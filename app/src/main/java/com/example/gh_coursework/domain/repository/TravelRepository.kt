package com.example.gh_coursework.domain.repository

import com.example.gh_coursework.domain.entity.PointDomain
import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointPreviewDomain
import kotlinx.coroutines.flow.Flow

interface TravelRepository {
    suspend fun addPointOfInterestDetails(poi: PointDomain)
    suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain)
    suspend fun addRoute(route: RouteDomain)
    suspend fun addRouteDetails(route: RouteDomain)
    fun getPointOfInterestPreview(id: Int): Flow<PointPreviewDomain>
    fun getRoutePreview(routeId: Int): Flow<List<RoutePointPreviewDomain>>
    fun getPointOfInterestDetails(id: Int): Flow<PointDomain>
    fun getRouteDetails(routeId: Int): Flow<RouteDomain>
}