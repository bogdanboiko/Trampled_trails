package com.example.gh_coursework.data.database

import com.example.gh_coursework.data.database.dao.PointDetailsDao
import com.example.gh_coursework.data.database.dao.PointPreviewDao
import com.example.gh_coursework.data.database.dao.RoutePreviewDao
import com.example.gh_coursework.data.database.dao.TagDao
import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.data.database.entity.RoutePointEntity
import com.example.gh_coursework.data.database.mapper.*
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.PointTagDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSrcIml(
    private val pointDao: PointPreviewDao,
    private val routeDao: RoutePreviewDao,
    private val pointDetailsDao: PointDetailsDao,
    private val tagDao: TagDao
) : TravelDatasource.Local {
    override suspend fun addOrUpdatePointOfInterestDetails(poi: PointDetailsDomain) {
        pointDetailsDao.updateOrInsertPointDetails(mapPointDetailsDomainToEntity(poi))
    }

    override suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain) {
        pointDao.addPointPreview(mapPointDomainToEntity(poi))
    }

    override suspend fun addRoute(
        route: RouteDomain,
        coordinatesList: List<PointCoordinatesEntity>
    ) {
        val routePointEntitiesList = mutableListOf<RoutePointEntity>()
        var position = 0

        coordinatesList.forEach {
            routePointEntitiesList.add(
                RoutePointEntity(
                    route.routeId?.toLong(),
                    pointDao.addPointPreview(PointCoordinatesEntity(it.pointId, it.x, it.y, true)),
                    position
                )
            )
            position++
        }

        routeDao.addRoute(mapRouteDomainToEntity(route), routePointEntitiesList)
    }

    override suspend fun deletePoint(pointId: Int) {
        pointDao.deletePoint(pointId)
    }

    override fun getPointOfInterestPreview(): Flow<List<PointPreviewDomain>> {
        return pointDao.getPointPreview()
            .map { pointPreview -> pointPreview.map(::mapPointEntityToDomain) }
    }

    override fun getRoutePreview(routeId: Int): Flow<List<PointPreviewDomain>> {
        TODO("Not yet implemented")
    }


    override fun getPointOfInterestDetails(id: Int): Flow<PointDetailsDomain?> {
        return pointDetailsDao.getPointDetails(id).map { mapPointDetailsEntityToDomain(it) }
    }

    override fun getRoute(): Flow<RouteDomain> {
        TODO("Not yet implemented")
    }

    override fun getRoutesList(): Flow<List<RouteDomain>> {
        return routeDao.getRoutesResponse()
            .map { it.map { entity -> (mapRouteResponseListToDomain(entity)) } }
    }

    override fun getPointTagList(): Flow<List<PointTagDomain>> {
        return tagDao.getPointTags().map { tagList -> tagList.map(::mapPointTagEntityToDomain) }
    }

    override suspend fun addPointTag(tag: PointTagDomain) {
        tagDao.addTag(mapTagDomainToEntity(tag))
    }
}