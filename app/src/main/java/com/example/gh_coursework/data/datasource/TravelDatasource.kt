package com.example.gh_coursework.data.datasource

import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface TravelDatasource {
    interface Local {
        suspend fun addOrUpdatePointOfInterestDetails(poi: PointDetailsDomain)
        suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain)
        suspend fun deletePoint(pointId: Long)

        suspend fun addPointTag(tag: PointTagDomain)
        suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>)
        suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>)
        suspend fun deletePointTag(tag: PointTagDomain)

        suspend fun addRoute(route: RouteDomain, coordinatesList: List<PointCoordinatesEntity>)
        suspend fun deleteRoute(route: RouteDomain)

        fun getPointsTagsList(pointId: Long): Flow<List<PointTagDomain>>
        fun getPointOfInterestPreview(): Flow<List<PointPreviewDomain>>
        fun getPointOfInterestDetails(id: Long): Flow<PointDetailsDomain?>
        fun getPointTagList(): Flow<List<PointTagDomain>>

        fun getRoutesList(): Flow<List<RouteDomain>>
    }

    interface Remote
}