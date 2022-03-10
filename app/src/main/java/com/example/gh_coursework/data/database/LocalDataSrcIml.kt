package com.example.gh_coursework.data.database

import com.example.gh_coursework.data.database.dao.PointDetailsDao
import com.example.gh_coursework.data.database.dao.PointPreviewDao
import com.example.gh_coursework.data.database.dao.RoutePreviewDao
import com.example.gh_coursework.data.database.mapper.mapPointDetailsDomainToEntity
import com.example.gh_coursework.data.database.mapper.mapPointDetailsEntityToDomain
import com.example.gh_coursework.data.database.mapper.mapPointDomainToEntity
import com.example.gh_coursework.data.database.mapper.mapPointEntityToDomain
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointPreviewDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSrcIml(private val pointDao: PointPreviewDao,
                      private val routeDao: RoutePreviewDao,
                      private val pointDetailsDao: PointDetailsDao) : TravelDatasource.Local {
    override suspend fun addOrUpdatePointOfInterestDetails(poi: PointDetailsDomain) {
        pointDetailsDao.updateOrInsertPointDetails(mapPointDetailsDomainToEntity(poi))
    }

    override suspend fun addPointOfInterestCoordinates(poi: PointPreviewDomain) {
        pointDao.addPointPreview(mapPointDomainToEntity(poi))
    }

    override suspend fun addRoute(route: RouteDomain) {
        TODO("Not yet implemented")
    }

    override suspend fun addRouteDetails(route: RouteDomain) {
        TODO("Not yet implemented")
    }


    override fun getPointOfInterestPreview() : Flow<List<PointPreviewDomain>> {
        return pointDao.getPointPreview().map { pointPreview -> pointPreview.map(::mapPointEntityToDomain) }
    }

    override fun getRoutePreview(routeId: Int): Flow<List<RoutePointPreviewDomain>> {
        TODO("Not yet implemented")
    }


    override fun getPointOfInterestDetails(id: Int): Flow<PointDetailsDomain?> {
        return pointDetailsDao.getPointDetails(id).map { mapPointDetailsEntityToDomain(it) }
    }

    override fun getRouteDetails(routeId: Int): Flow<RouteDomain> {
        TODO("Not yet implemented")
    }

}