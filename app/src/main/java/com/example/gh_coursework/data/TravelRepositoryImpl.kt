package com.example.gh_coursework.data

import com.example.gh_coursework.data.database.mapper.point_preview.mapPointDomainToEntity
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.domain.entity.*
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class TravelRepositoryImpl(private val localDataSrcIml: TravelDatasource.Local) : TravelRepository {

    //PointPreview
    override suspend fun addPointOfInterestCoordinatesWithDetails(poi: PointPreviewDomain) {
        localDataSrcIml.addPointOfInterestCoordinates(poi)
    }

    override fun getPointOfInterestPreview() = localDataSrcIml.getPointOfInterestPreview()

    //PointDetails
    override suspend fun addOrUpdatePointOfInterestDetails(poi: PointDetailsDomain) {
        localDataSrcIml.addOrUpdatePointOfInterestDetails(poi)
    }

    override suspend fun addPointImages(images: List<PointImageDomain>) {
        localDataSrcIml.addPointImages(images)
    }

    override suspend fun deletePointImage(image: PointImageDomain) {
        localDataSrcIml.deletePointImage(image)
    }

    override fun getPointOfInterestDetails(id: Long) = localDataSrcIml.getPointOfInterestDetails(id)

    override fun getPointImages(pointId: Long) = localDataSrcIml.getPointImages(pointId)

    //PointTag
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

    override fun getPointTagList() = localDataSrcIml.getPointTagList()

    override fun getPointsTagsList(pointId: Long) = localDataSrcIml.getPointsTagsList(pointId)

    override suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>) {
        localDataSrcIml.removePointsTagsList(pointsTagsList)
    }

    //RoutePreview
    override suspend fun addRoute(route: RouteDomain) {
        localDataSrcIml.addRoute(route, route.coordinatesList.map(::mapPointDomainToEntity))
    }

    override suspend fun deleteRoute(route: RouteDomain) {
        localDataSrcIml.deleteRoute(route)
    }

    override fun getRoutesList() = localDataSrcIml.getRoutesList()

    //RouteDetails
    override fun getRouteDetails(routeId: Long) = localDataSrcIml.getRouteDetails(routeId)

    override suspend fun updateRoute(route: RouteDetailsDomain) {
        localDataSrcIml.updateRoute(route)
    }

    //RouteTag
    override suspend fun addRouteTagsList(routeTagsList: List<RouteTagsDomain>) {
        localDataSrcIml.addRouteTagsList(routeTagsList)
    }

    override suspend fun deleteTagsFromRoute(routeTagsList: List<RouteTagsDomain>) {
        localDataSrcIml.deleteTagsFromRoute(routeTagsList)
    }

    override fun getRouteTags() = localDataSrcIml.getRouteTags()
}