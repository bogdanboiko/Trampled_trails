package com.example.gh_coursework.ui.public_route

import androidx.lifecycle.ViewModel
import com.example.gh_coursework.domain.usecase.route_points.GetRoutePointsListUseCase
import com.example.gh_coursework.domain.usecase.route_preview.GetRoutesListUseCase
import com.example.gh_coursework.ui.public_route.mapper.mapRouteDomainToModel
import com.example.gh_coursework.ui.public_route.mapper.mapRoutePointDomainToModel
import com.example.gh_coursework.ui.public_route.model.RoutePointModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PublicRouteViewModel(
    getRoutesListUseCase: GetRoutesListUseCase,
    private val getRoutePointsListUseCase: GetRoutePointsListUseCase
) : ViewModel() {

    val routes = getRoutesListUseCase.invoke()
        .map { route -> route.map(::mapRouteDomainToModel) }

    fun getRoutePointsList(routeId: Long): Flow<List<RoutePointModel>> {
        return getRoutePointsListUseCase.invoke(routeId).map {
            it.map(::mapRoutePointDomainToModel)
        }
    }
}