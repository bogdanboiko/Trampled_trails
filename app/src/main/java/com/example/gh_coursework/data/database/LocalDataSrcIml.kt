package com.example.gh_coursework.data.database

import com.example.gh_coursework.data.database.dao.PointPreviewDao
import com.example.gh_coursework.data.database.dao.RoutePreviewDao
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.domain.entity.PointOfInterestDomain
import com.example.gh_coursework.domain.entity.PointOfInterestPreviewDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointPreviewDomain
import kotlinx.coroutines.flow.Flow

class LocalDataSrcIml(private val pointDao: PointPreviewDao,
                      private val routeDao: RoutePreviewDao) : TravelDatasource.Local {
    override suspend fun addPointOfInterestDetails(poi: PointOfInterestDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun addPointOfInterestCoordinates(poi: PointOfInterestPreviewDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun addRoute(route: RouteDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun addRouteDetails(route: RouteDomain) {
        TODO("Not yet implemented")
    }


    override fun getPointOfInterestPreview(id: Int): Flow<PointOfInterestPreviewDomain> {
        TODO("Not yet implemented")
    }

    override fun getRoutePreview(routeId: Int): Flow<List<RoutePointPreviewDomain>> {
        TODO("Not yet implemented")
    }


    override fun getPointOfInterestDetails(id: Int): Flow<PointOfInterestDomain> {
        TODO("Not yet implemented")
    }

    override fun getRouteDetails(routeId: Int): Flow<RouteDomain> {
        TODO("Not yet implemented")
    }

}