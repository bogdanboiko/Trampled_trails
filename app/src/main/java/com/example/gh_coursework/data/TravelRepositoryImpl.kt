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

    override suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain) {
        localDataSrcIml.addPointOfInterestCoordinates(poi)
    }

    override suspend fun deletePoint(pointId: Int) {
        localDataSrcIml.deletePoint(pointId)
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

    override suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>) {
        localDataSrcIml.removePointsTagsList(pointsTagsList)
    }

    override fun getPointOfInterestPreview() = localDataSrcIml.getPointOfInterestPreview()

    override fun getRoutePreview(routeId: Int): Flow<List<PointPreviewDomain>> {
        TODO("Not yet implemented")
    }

    override fun getPointOfInterestDetails(id: Int) = localDataSrcIml.getPointOfInterestDetails(id)

    override fun getRoute(): Flow<RouteDomain> = localDataSrcIml.getRoute()

    override fun getRoutesList() = localDataSrcIml.getRoutesList()

    override fun getPointTagList() = localDataSrcIml.getPointTagList()
}