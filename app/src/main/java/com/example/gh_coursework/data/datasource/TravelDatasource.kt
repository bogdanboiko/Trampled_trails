package com.example.gh_coursework.data.datasource

import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.domain.entity.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface TravelDatasource {
    interface Local {
        suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain)
        fun getAllPointsDetails(): Flow<List<PointCompleteDetailsDomain>>
        suspend fun deletePoint(pointId: String)

        suspend fun addPointTag(tag: PointTagDomain)
        suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>)
        suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>)
        suspend fun deletePointTag(tag: PointTagDomain)

        suspend fun addPointImages(images: List<PointImageDomain>)
        suspend fun addRouteImages(images: List<RouteImageDomain>)
        suspend fun deletePointImage(image: PointImageDomain)
        suspend fun deleteRouteImage(image: RouteImageDomain)

        suspend fun addRoute(route: RouteDomain, coordinatesList: List<PointCoordinatesEntity>)
        suspend fun updateRoute(route: RouteDomain)
        suspend fun deleteRoute(route: RouteDomain)

        suspend fun addRouteTagsList(routeTagsList: List<RouteTagsDomain>)
        suspend fun deleteTagsFromRoute(routeTagsList: List<RouteTagsDomain>)

        fun getPointsTagsList(pointId: String): Flow<List<PointTagDomain>>
        fun getPointOfInterestDetails(id: String): Flow<PointDetailsDomain?>
        fun getPointTagList(): Flow<List<PointTagDomain>>

        fun getPointImages(pointId: String): Flow<List<PointImageDomain>>
        fun getRouteImages(routeId: String): Flow<List<RouteImageDomain>>

        fun getRoutesList(): Flow<List<RouteDomain>>
        fun getPublicRoutesList(): Flow<List<RouteDomain>>
        fun getRouteDetails(routeId: String): Flow<RouteDomain>
        fun getRoutePointsList(routeId: String): Flow<List<RoutePointDomain>>
        fun getRoutePointsImagesList(routeId: String): Flow<List<RoutePointsImagesDomain>>

        fun getRouteTags(): Flow<List<RouteTagDomain>>
        suspend fun updatePointOfInterestDetails(poi: PointDetailsDomain)

        suspend fun saveFirebaseRouteToLocal(route: PublicRouteDomain, points: List<PublicRoutePointDomain>)
    }

    interface Remote {
        suspend fun publishRoute(
            route: RouteDomain,
            routePoints: List<RoutePointDomain>,
            currentUser: String
        )

        fun fetchRoutePoints(routeId: String): Flow<List<PublicRoutePointDomain>>
        suspend fun savePointImages(imageList: List<PointImageDomain>, pointId: String, routeId: String)
        suspend fun saveRouteImages(imageList: List<RouteImageDomain>, routeId: String)
        fun getUserRoutes(userId: String): Flow<List<PublicRouteDomain>>
    }
}