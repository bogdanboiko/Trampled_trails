package com.example.gh_coursework.data.datasource

import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.data.remote.entity.PublicFavouriteEntity
import com.example.gh_coursework.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface TravelDatasource {
    interface Local {
        suspend fun addDeletedPoint(point: DeletedPointDomain)
        suspend fun addDeletedRoute(route: DeletedRouteDomain)
        suspend fun clearDeletedPointsTable()
        suspend fun clearDeletedRoutesTable()
        suspend fun clearDeletedImagesTable()
        fun getDeletedRoutes(): Flow<List<DeletedRouteDomain>>
        fun getDeletedPoints(): Flow<List<DeletedPointDomain>>

        suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain)
        suspend fun updatePointOfInterestDetails(poi: PointDetailsDomain)
        suspend fun deletePoint(pointId: String)
        suspend fun deleteAllPoints()
        fun getAllPoints(): Flow<List<PointDomain>>

        suspend fun addPointsTagsList(pointsTagsList: List<PointsTagsDomain>)
        suspend fun removePointsTagsList(pointsTagsList: List<PointsTagsDomain>)

        suspend fun addPointImages(images: List<PointImageDomain>)
        suspend fun addRouteImages(images: List<RouteImageDomain>)
        suspend fun deletePointImage(image: PointImageDomain)
        suspend fun deleteRouteImage(image: RouteImageDomain)

        suspend fun addRoute(route: RouteDomain, coordinatesList: List<PointCoordinatesEntity>)
        suspend fun updateRoute(route: RouteDomain)
        suspend fun deleteRoute(route: RouteDomain)
        suspend fun deleteAllRoutes()

        suspend fun addRouteTagsList(routeTagsList: List<RouteTagsDomain>)
        suspend fun deleteTagsFromRoute(routeTagsList: List<RouteTagsDomain>)

        fun getPointsTagsList(pointId: String): Flow<List<PointTagDomain>>
        fun getPointOfInterestDetails(id: String): Flow<PointDetailsDomain?>
        fun getAllPointsDetails(): Flow<List<PointDomain>>
        fun getPointTagList(): Flow<List<PointTagDomain>>

        fun getPointImages(pointId: String): Flow<List<PointImageDomain>>
        fun getRouteImages(routeId: String): Flow<List<RouteImageDomain>>

        fun getRoutesList(): Flow<List<RouteDomain>>
        fun getRouteDetails(routeId: String): Flow<RouteDomain>
        fun getRoutePointsList(routeId: String): Flow<List<PointDomain>>
        fun getRoutePointsImagesList(routeId: String): Flow<List<RoutePointsImagesDomain>>
        fun getRouteTags(): Flow<List<RouteTagDomain>>

        fun changeRouteAccess(routeId: String, isPublic: Boolean)

        suspend fun saveFirebasePointsToLocal(points: List<PublicPointDomain>)
        suspend fun saveFirebaseRouteToLocal(route: PublicRouteDomain)
        fun getImageListToDelete(): Flow<List<String>>
        fun addImageToDelete(imageUrl: String)
    }

    interface Remote {
        suspend fun deletePoint(pointId: String)
        suspend fun deleteRoute(routeId: String)

        suspend fun uploadRouteToFirebase(route: RouteDomain, currentUser: String)
        suspend fun uploadPointsToFirebase(points: List<PointDomain>, currentUser: String)

        suspend fun savePointImages(imageList: List<PointImageDomain>, pointId: String)
        suspend fun saveRouteImages(imageList: List<RouteImageDomain>, routeId: String)
        fun deleteImagesFromFirebase(images: List<String>)

        fun fetchRoutePoints(routeId: String): Flow<List<PublicPointDomain>>
        fun getPublicRoutes(): Flow<List<PublicRouteDomain>>

        fun getAllFavouriteRoutes(): Flow<List<PublicFavouriteEntity>>
        fun getUserFavouriteRoutes(userId: String): Flow<List<String>>
        suspend fun addRouteToFavourites(routeId: String, userId: String)
        suspend fun removeRouteFromFavourites(routeId: String, userId: String)

        fun getUserRoutes(userId: String): Flow<List<PublicRouteDomain>>
        fun getUserPoints(userId: String): Flow<List<PublicPointDomain>>

        fun changeRouteAccess(routeId: String, isPublic: Boolean)
    }
}