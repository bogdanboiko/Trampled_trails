package com.example.gh_coursework.data.datasource

import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface TravelDatasource {
    interface Local {
        suspend fun addOrUpdatePointOfInterestDetails(poi: PointDetailsDomain)
        suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain)
        suspend fun addRoute(route: RouteDomain, coordinatesList: List<PointCoordinatesEntity>)
        suspend fun deletePoint(pointId: Int)
        suspend fun addPointTag(tag: PointTagDomain)
        suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>)
        suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>)
        fun getPointOfInterestPreview(): Flow<List<PointPreviewDomain>>
        fun getRoutePreview(routeId: Int): Flow<List<PointPreviewDomain>>
        fun getPointOfInterestDetails(id: Int): Flow<PointDetailsDomain?>
        fun getRoute(): Flow<RouteDomain>
        fun getRoutesList(): Flow<List<RouteDomain>>
        fun getPointTagList(): Flow<List<PointTagDomain>>
    }

    interface Remote
}