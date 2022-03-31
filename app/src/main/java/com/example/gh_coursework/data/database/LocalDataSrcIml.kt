package com.example.gh_coursework.data.database

import com.example.gh_coursework.data.database.dao.*
import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.data.database.entity.RoutePointEntity
import com.example.gh_coursework.data.database.mapper.images.mapPointImageEntityToDomain
import com.example.gh_coursework.data.database.mapper.images.mapRouteImageDomainToEntity
import com.example.gh_coursework.data.database.mapper.images.mapRouteImageEntityToDomain
import com.example.gh_coursework.data.database.mapper.mapPointImageDomainToEntity
import com.example.gh_coursework.data.database.mapper.point_details.mapPointDetailsDomainToEntity
import com.example.gh_coursework.data.database.mapper.point_details.mapPointDetailsEntityToDomain
import com.example.gh_coursework.data.database.mapper.point_preview.mapPointDomainToEntity
import com.example.gh_coursework.data.database.mapper.point_preview.mapPointEntityToDomain
import com.example.gh_coursework.data.database.mapper.point_tag.mapPointTagEntityToDomain
import com.example.gh_coursework.data.database.mapper.point_tag.mapPointsTagsDomainToEntity
import com.example.gh_coursework.data.database.mapper.point_tag.mapTagDomainToEntity
import com.example.gh_coursework.data.database.mapper.route_details.mapRoutePointsImagesResponseToDomain
import com.example.gh_coursework.data.database.mapper.route_preview.mapRouteDomainToEntity
import com.example.gh_coursework.data.database.mapper.route_preview.mapRoutePointEntityToDomain
import com.example.gh_coursework.data.database.mapper.route_preview.mapRouteResponseToDomain
import com.example.gh_coursework.data.database.mapper.route_tag.mapRouteTagEntityToDomain
import com.example.gh_coursework.data.database.mapper.route_tag.mapRouteTagsDomainToEntity
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.domain.entity.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSrcIml(
    private val pointDao: PointPreviewDao,
    private val routeDao: RoutePreviewDao,
    private val pointDetailsDao: PointDetailsDao,
    private val tagDao: PointTagDao,
    private val imageDao: ImageDao,
    private val routeTagDao: RouteTagDao
) : TravelDatasource.Local {

    //PointPreview
    override suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain): Long {
        val pointId = pointDao.addPointPreview(mapPointDomainToEntity(poi))
        addOrUpdatePointOfInterestDetails(
            PointDetailsDomain(
                pointId,
                emptyList(),
                emptyList(),
                "",
                ""
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

    override suspend fun addPointImages(images: List<PointImageDomain>) {
        imageDao.addPointImages(images.map(::mapPointImageDomainToEntity))
    }

    override suspend fun deletePointImage(image: PointImageDomain) {
        imageDao.deletePointImage(mapPointImageDomainToEntity(image))
    }

    override fun getPointOfInterestDetails(id: Long): Flow<PointDetailsDomain?> {
        return pointDetailsDao.getPointDetails(id).map { mapPointDetailsEntityToDomain(it) }
    }

    override fun getPointImages(pointId: Long): Flow<List<PointImageDomain>> {
        return imageDao.getPointImages(pointId).map { it.map(::mapPointImageEntityToDomain) }
    }

    //PointTag
    override suspend fun addPointTag(tag: PointTagDomain) {
        tagDao.addTag(mapTagDomainToEntity(tag))
    }

    override suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>) {
        tagDao.addTagsToPoint(pointsTagsList.map(::mapPointsTagsDomainToEntity))
    }

    override suspend fun deletePointTag(tag: PointTagDomain) {
        tagDao.deleteTag(mapTagDomainToEntity(tag))
    }

    override fun getPointTagList(): Flow<List<PointTagDomain>> {
        return tagDao.getPointTags()
            .map { tagList -> tagList.map(::mapPointTagEntityToDomain) }
    }

    override fun getPointsTagsList(pointId: Long): Flow<List<PointTagDomain>> {
        return getPointOfInterestDetails(pointId).map { it?.tagList ?: emptyList() }
    }

    override suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>) {
        tagDao.deleteTagsFromPoint(pointsTagsList.map(::mapPointsTagsDomainToEntity))
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

    override fun getRouteDetails(routeId: Long): Flow<RouteDomain> {
        return routeDao.getRouteDetails(routeId).map(::mapRouteResponseToDomain)
    }

    @OptIn(FlowPreview::class)
    override fun getRoutesList(): Flow<List<RouteDomain>> {
        return routeDao.getRoutesResponse().map { it.map(::mapRouteResponseToDomain) }
    }

    override fun getRoutePointsList(routeId: Long): Flow<List<RoutePointDomain>> {
        return routeDao.getRoutePoints(routeId).map { it.map(::mapRoutePointEntityToDomain) }
    }

    override suspend fun updateRoute(route: RouteDomain) {
        routeDao.updateRouteDetails(mapRouteDomainToEntity(route))
    }

    //RouteImage
    override suspend fun addRouteImages(images: List<RouteImageDomain>) {
        imageDao.addRouteImages(images.map(::mapRouteImageDomainToEntity))
    }

    override suspend fun deleteRouteImage(image: RouteImageDomain) {
        imageDao.deleteRouteImage(mapRouteImageDomainToEntity(image))
    }

    override fun getRouteImages(routeId: Long): Flow<List<RouteImageDomain>> {
        return imageDao.getRouteImages(routeId).map { image ->
            image.map(::mapRouteImageEntityToDomain)
        }
    }

    override fun getRoutePointsImagesList(routeId: Long): Flow<List<RoutePointsImagesDomain>> {
        return routeDao.getRoutePointsImages(routeId).map { image ->
            image.map(::mapRoutePointsImagesResponseToDomain)
        }
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