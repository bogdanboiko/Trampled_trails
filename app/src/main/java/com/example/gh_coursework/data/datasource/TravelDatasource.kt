package com.example.gh_coursework.data.datasource

import com.example.gh_coursework.domain.entity.PointOfInterestDomain
import com.example.gh_coursework.domain.entity.PointOfInterestPreviewDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointPreviewDomain
import kotlinx.coroutines.flow.Flow

interface TravelDatasource {
    interface Local {
        suspend fun addPointOfInterestDetails(poi: PointOfInterestDomain)
        suspend fun addPointOfInterestCoordinates(poi: PointOfInterestPreviewDomain)
        suspend fun addRoute(route: RouteDomain)
        suspend fun addRouteDetails(route: RouteDomain)
        fun getPointOfInterestPreview(id: Int): Flow<PointOfInterestPreviewDomain>
        fun getRoutePreview(routeId: Int): Flow<List<RoutePointPreviewDomain>>
        fun getPointOfInterestDetails(id: Int): Flow<PointOfInterestDomain>
        fun getRouteDetails(routeId: Int): Flow<RouteDomain>
    }

    interface Remote
}