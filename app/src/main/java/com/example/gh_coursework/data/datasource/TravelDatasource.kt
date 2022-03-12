package com.example.gh_coursework.data.datasource

import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import kotlinx.coroutines.flow.Flow

interface TravelDatasource {
    interface Local {
        suspend fun addOrUpdatePointOfInterestDetails(poi: PointDetailsDomain)
        suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain)
        suspend fun addRoute(route: RouteDomain, coordinatesList: List<PointCoordinatesEntity>)
        suspend fun deletePoint(pointId: Int)
        fun getPointOfInterestPreview(): Flow<List<PointPreviewDomain>>
        fun getRoutePreview(routeId: Int): Flow<List<PointPreviewDomain>>
        fun getPointOfInterestDetails(id: Int): Flow<PointDetailsDomain?>
        fun getRoute(): Flow<RouteDomain>
        fun getRoutesList(): Flow<List<RouteDomain>>
    }

    interface Remote
}