package com.example.gh_coursework.ui.private_route.mapper

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.ui.private_route.model.PrivateRouteModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointModel

fun mapRouteDomainToModel(routeDomain: RouteDomain): PrivateRouteModel {
    val pointCoordinatesModelList = mutableListOf<PrivateRoutePointModel>()

    routeDomain.coordinatesList.forEach {
        pointCoordinatesModelList.add(mapPointDomainToModel(it))
    }

    return PrivateRouteModel(routeDomain.routeId, pointCoordinatesModelList)
}