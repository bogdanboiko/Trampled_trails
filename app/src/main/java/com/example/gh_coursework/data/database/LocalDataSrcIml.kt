package com.example.gh_coursework.data.database

import com.example.gh_coursework.data.database.dao.*
import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.data.database.entity.RoutePointEntity
import com.example.gh_coursework.data.database.mapper.mapPointDetailsDomainToEntity
import com.example.gh_coursework.data.database.mapper.mapPointDetailsEntityToDomain
import com.example.gh_coursework.data.database.mapper.point_preview.mapPointDomainToEntity
import com.example.gh_coursework.data.database.mapper.point_preview.mapPointEntityToDomain
import com.example.gh_coursework.data.database.mapper.point_tag.mapPointTagEntityToDomain
import com.example.gh_coursework.data.database.mapper.point_tag.mapPointsTagsDomainToEntity
import com.example.gh_coursework.data.database.mapper.point_tag.mapTagDomainToEntity
import com.example.gh_coursework.data.database.mapper.route_details.mapRouteDetailsDomainToEntity
import com.example.gh_coursework.data.database.mapper.route_details.mapRouteDetailsResponseToDomain
import com.example.gh_coursework.data.database.mapper.route_preview.mapRouteDomainToEntity
import com.example.gh_coursework.data.database.mapper.route_preview.mapRouteResponseListToDomain
import com.example.gh_coursework.data.database.mapper.route_tag.mapRouteTagEntityToDomain
import com.example.gh_coursework.data.database.mapper.route_tag.mapRouteTagsDomainToEntity
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.domain.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSrcIml(
    private val pointDao: PointPreviewDao,
    private val pointDetailsDao: PointDetailsDao,
    private val pointPointTagDao: PointTagDao,
    private val routeDao: RoutePreviewDao,
    private val routeTagDao: RouteTagDao
) : TravelDatasource.Local {

    //PointPreview
    override suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain): Long {
        val pointId = pointDao.addPointPreview(mapPointDomainToEntity(poi))
        addOrUpdatePointOfInterestDetails(
            PointDetailsDomain(
                pointId,
                emptyList(),
                "Empty caption",
                "Empty description"
            )
        )

        return pointId
    }

    override fun getPointOfInterestPreview(): Flow<List<PointPreviewDomain>> {
        return pointDao.getPointPreview()
            .map { pointPreview -> pointPreview.map(::mapPointEntityToDomain) }
    }

    override suspend fun deletePoint(pointId: Long) {
        pointDao.deletePoint(pointId)
    }

    //PointDetails
    override suspend fun addOrUpdatePointOfInterestDetails(poi: PointDetailsDomain) {
        pointDetailsDao.updateOrInsertPointDetails(mapPointDetailsDomainToEntity(poi))
    }

    override fun getPointOfInterestDetails(id: Long): Flow<PointDetailsDomain?> {
        return pointDetailsDao.getPointDetails(id).map { mapPointDetailsEntityToDomain(it) }
    }

    //PointTag
    override suspend fun addPointTag(tag: PointTagDomain) {
        pointPointTagDao.addTag(mapTagDomainToEntity(tag))
    }

    override suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>) {
        pointPointTagDao.addTagsToPoint(pointsTagsList.map(::mapPointsTagsDomainToEntity))
    }

    override suspend fun deletePointTag(tag: PointTagDomain) {
        pointPointTagDao.deleteTag(mapTagDomainToEntity(tag))
    }

    override fun getPointTagList(): Flow<List<PointTagDomain>> {
        return pointPointTagDao.getPointTags()
            .map { tagList -> tagList.map(::mapPointTagEntityToDomain) }
    }

    override fun getPointsTagsList(pointId: Long): Flow<List<PointTagDomain>> {
        return getPointOfInterestDetails(pointId).map { it?.tagList ?: emptyList() }
    }

    override suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>) {
        pointPointTagDao.deleteTagsFromPoint(pointsTagsList.map(::mapPointsTagsDomainToEntity))
    }

    //RoutePreview
    override suspend fun addRoute(
        route: RouteDomain,
        coordinatesList: List<PointCoordinatesEntity>
    ) {
        val routePointEntitiesList = mutableListOf<RoutePointEntity>()
        var position = 0

        coordinatesList.forEach {
            routePointEntitiesList.add(
                RoutePointEntity(
                    route.routeId,
                    addPointOfInterestCoordinates(mapPointEntityToDomain(it)),
                    position
                )
            )
            position++
        }

        routeDao.addRoute(mapRouteDomainToEntity(route), routePointEntitiesList)
    }

    override suspend fun deleteRoute(route: RouteDomain) {
        routeDao.deleteRoute(mapRouteDomainToEntity(route))
    }

    override fun getRoutesList(): Flow<List<RouteDomain>> {
        return routeDao.getRoutesResponse()
            .map { it.map { entity -> (mapRouteResponseListToDomain(entity)) } }
    }

    override fun getRouteDetails(routeId: Long): Flow<RouteDetailsDomain> {
        return routeDao.getRouteDetails(routeId).map(::mapRouteDetailsResponseToDomain)
    }

    override suspend fun updateRoute(route: RouteDetailsDomain) {
        routeDao.updateRouteDetails(mapRouteDetailsDomainToEntity(route))
    }

    //RouteTag
    override suspend fun addRouteTagsList(routeTagsList: List<RouteTagsDomain>) {
        routeTagDao.addRouteTags(routeTagsList.map(::mapRouteTagsDomainToEntity))
    }

    override suspend fun deleteTagsFromRoute(routeTagsList: List<RouteTagsDomain>) {
        routeTagDao.deleteTagsFromRoute(routeTagsList.map(::mapRouteTagsDomainToEntity))
    }

    override fun getRouteTags(): Flow<List<RouteTagDomain>> {

        return routeTagDao.getTagsList()
            .map { it.map(::mapRouteTagEntityToDomain) }
    }
}