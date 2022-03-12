package com.example.gh_coursework.data.database.mapper

import com.example.gh_coursework.data.database.response.RoutePreviewResponse
import com.example.gh_coursework.domain.entity.PointPreviewDomain
import com.example.gh_coursework.domain.entity.RouteDomain

fun mapRouteResponseListToDomain(routeResponse: List<RoutePreviewResponse?>): List<RouteDomain> {
    val routeDomainList = mutableListOf<RouteDomain>()
    var coordinatesDomainList = mutableListOf<PointPreviewDomain>()
    var routesIdList = mutableListOf<Int>()

    if (routeResponse.isNotEmpty()) {

        //Collect all routeId
        routeResponse.forEach {
            it?.route?.routeId?.let { routeId -> routesIdList.add(routeId) }
        }

        //Delete all non-unique routeId
        routesIdList = routesIdList.distinct() as MutableList<Int>

        routesIdList.forEach {

            //Creating coordinates list of routeId == it
            for (point in routeResponse) {
                if (point?.route?.routeId == it) {
                    //coordinatesDomainList.add(mapRoutePointEntityToDomain(point.coordinate)) is below

                    point.coordinate?.let { it1 ->
                        mapRoutePointEntityToDomain(
                            it1
                        )
                    }?.let { it2 -> coordinatesDomainList.add(it2) }
                }
            }

            routeDomainList.add(
                RouteDomain(
                    routeResponse[it]?.route?.routeId,
                    routeResponse[it]?.route?.name,
                    routeResponse[it]?.route?.description,
                    routeResponse[it]?.route?.rating,
                    coordinatesDomainList
                )
            )

            coordinatesDomainList = mutableListOf()
        }

        return routeDomainList
    }

    return listOf(RouteDomain(0, "", "", 0.0, listOf()))
}