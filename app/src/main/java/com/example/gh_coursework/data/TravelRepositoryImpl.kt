package com.example.gh_coursework.data

import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.domain.entity.PointOfInterestDomain
import com.example.gh_coursework.domain.entity.PointOfInterestPreviewDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointPreviewDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class TravelRepositoryImpl(private val localDataSrcIml: TravelDatasource.Local) : TravelRepository {
    override suspend fun addPointOfInterestDetails(poi: PointOfInterestDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun addPointOfInterestCoordinates(poi: PointOfInterestPreviewDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun addRoute(route: RouteDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun addRouteDetails(route: List<RoutePointPreviewDomain>) {
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