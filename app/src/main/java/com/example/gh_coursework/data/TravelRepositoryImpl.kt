package com.example.gh_coursework.data

import com.example.gh_coursework.data.database.mapper.mapPointDomainToEntity
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.domain.entity.*
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class TravelRepositoryImpl(private val localDataSrcIml: TravelDatasource.Local) : TravelRepository {
    override suspend fun addOrUpdatePointOfInterestDetails(poi: PointDetailsDomain) {
        localDataSrcIml.addOrUpdatePointOfInterestDetails(poi)
    }

    override suspend fun addPointOfInterestCoordinatesWithDetails(poi: PointPreviewDomain) {
        localDataSrcIml.addPointOfInterestCoordinatesWithDetails(poi)
    }

    override suspend fun addRoute(route: RouteDomain) {
        localDataSrcIml.addRoute(route, route.coordinatesList.map(::mapPointDomainToEntity))
    }

    override suspend fun addPointTag(tag: PointTagDomain) {
        localDataSrcIml.addPointTag(tag)
    }

    override suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>) {
        localDataSrcIml.addPointsTagsList(pointsTagsList)
    }

    override suspend fun deletePoint(pointId: Long) {
        localDataSrcIml.deletePoint(pointId)
    }

    override suspend fun deletePointTag(tag: PointTagDomain) {
        localDataSrcIml.deletePointTag(tag)
    }

    override suspend fun deleteRoute(route: RouteDomain) {
        localDataSrcIml.deleteRoute(route)
    }

    override fun getPointsTagsList(pointId: Long): Flow<List<PointTagDomain>> {
        return localDataSrcIml.getPointsTagsList(pointId)
    }

    override suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>) {
        localDataSrcIml.removePointsTagsList(pointsTagsList)
    }

    override fun getPointOfInterestPreview() = localDataSrcIml.getPointOfInterestPreview()

    override fun getPointOfInterestDetails(id: Long) = localDataSrcIml.getPointOfInterestDetails(id)

    override fun getPointTagList() = localDataSrcIml.getPointTagList()

    override fun getRoutesList() = localDataSrcIml.getRoutesList()
}