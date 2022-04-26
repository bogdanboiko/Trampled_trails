package com.example.gh_coursework.data

import com.example.gh_coursework.data.database.mapper.route_preview.mapRoutePointDomainToEntity
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.domain.entity.*
import com.example.gh_coursework.domain.repository.TravelRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class TravelRepositoryImpl(
    private val localDataSrcIml: TravelDatasource.Local,
    private val remoteDataSrcImpl: TravelDatasource.Remote
) : TravelRepository {

    //PointPreview
    override suspend fun addPointOfInterestCoordinatesWithDetails(poi: PointPreviewDomain) {
        localDataSrcIml.addPointOfInterestCoordinates(poi)
    }

    //PointDetails
    override suspend fun updatePointOfInterestDetails(poi: PointDetailsDomain) {
        localDataSrcIml.updatePointOfInterestDetails(poi)
    }

    override fun getAllPointsDetails() = localDataSrcIml.getAllPointsDetails()
    override suspend fun savePublicRouteToPrivate(
        route: PublicRouteDomain,
        points: List<PublicRoutePointDomain>
    ) {
        localDataSrcIml.savePublicRouteToPrivate(route, points)
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
    override suspend fun addRoute(route: RouteDomain, coordinatesList: List<RoutePointDomain>) {
        localDataSrcIml.addRoute(route, coordinatesList.map(::mapRoutePointDomainToEntity))
    }

    override suspend fun deleteRoute(route: RouteDomain) {
        localDataSrcIml.deleteRoute(route)
    }

    override fun getRoutesList() = localDataSrcIml.getRoutesList()

    override fun getPublicRoutesList(): Flow<List<RouteDomain>> = localDataSrcIml.getPublicRoutesList()

    //RouteDetails
    override fun getRouteDetails(routeId: Long) = localDataSrcIml.getRouteDetails(routeId)

    override fun getRoutePointsList(routeId: Long) = localDataSrcIml.getRoutePointsList(routeId)

    override suspend fun updateRoute(route: RouteDomain) {
        localDataSrcIml.updateRoute(route)
    }

    //RouteImage
    override suspend fun addRouteImages(images: List<RouteImageDomain>) {
        localDataSrcIml.addRouteImages(images)
    }

    override suspend fun deleteRouteImage(image: RouteImageDomain) {
        localDataSrcIml.deleteRouteImage(image)
    }

    override fun getRouteImages(routeId: Long) = localDataSrcIml.getRouteImages(routeId)

    override fun getRoutePointsImagesList(routeId: Long) =
        localDataSrcIml.getRoutePointsImagesList(routeId)

    //RouteTag
    override suspend fun addRouteTagsList(routeTagsList: List<RouteTagsDomain>) {
        localDataSrcIml.addRouteTagsList(routeTagsList)
    }

    override suspend fun deleteTagsFromRoute(routeTagsList: List<RouteTagsDomain>) {
        localDataSrcIml.deleteTagsFromRoute(routeTagsList)
    }

    override fun getRouteTags() = localDataSrcIml.getRouteTags()

    override fun publishRoute(
        route: RouteDomain,
        routePoints: List<RoutePointDomain>,
        currentUser: FirebaseUser
    ) {
        remoteDataSrcImpl.publishRoute(route, routePoints, currentUser)
    }

    override fun fetchRoutePoints(routeId: String): Flow<List<PublicRoutePointDomain>> {
        return remoteDataSrcImpl.fetchRoutePoints(routeId)
    }
}