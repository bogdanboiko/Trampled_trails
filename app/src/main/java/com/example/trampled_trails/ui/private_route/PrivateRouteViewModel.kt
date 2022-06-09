package com.example.trampled_trails.ui.private_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trampled_trails.data.remote.mapper.mapRoutePointModelToPointDetailsDomain
import com.example.trampled_trails.domain.usecase.point_preview.DeletePointUseCase
import com.example.trampled_trails.domain.usecase.public.ChangeRouteAccessUseCase
import com.example.trampled_trails.domain.usecase.route_points.GetRoutePointsListUseCase
import com.example.trampled_trails.domain.usecase.route_preview.AddRouteUseCase
import com.example.trampled_trails.domain.usecase.route_preview.DeleteRouteUseCase
import com.example.trampled_trails.domain.usecase.route_preview.GetRoutesListUseCase
import com.example.trampled_trails.ui.private_route.mapper.mapRouteDomainToModel
import com.example.trampled_trails.ui.private_route.mapper.mapRouteModelToDomain
import com.example.trampled_trails.ui.private_route.mapper.mapRoutePointDomainToModel
import com.example.trampled_trails.ui.private_route.mapper.mapRoutePointModelToDomain
import com.example.trampled_trails.ui.private_route.model.RouteModel
import com.example.trampled_trails.ui.private_route.model.RoutePointModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PrivateRouteViewModel(
    getRoutesListUseCase: GetRoutesListUseCase,
    private val addRouteUseCase: AddRouteUseCase,
    private val getRoutePointsListUseCase: GetRoutePointsListUseCase,
    private val deletePointUseCase: DeletePointUseCase,
    private val deleteRouteUseCase: DeleteRouteUseCase,
    private val changeRouteAccessUseCase: ChangeRouteAccessUseCase
) : ViewModel() {

    val routes = getRoutesListUseCase.invoke()
        .map { route -> route.map(::mapRouteDomainToModel) }

    fun addRoute(route: RouteModel, pointsList: List<RoutePointModel>) {
        viewModelScope.launch {
            addRouteUseCase.invoke(
                mapRouteModelToDomain(route),
                pointsList.map(::mapRoutePointModelToDomain)
            )
        }
    }

    fun getRoutePointsList(routeId: String): Flow<List<RoutePointModel>> {
        return getRoutePointsListUseCase.invoke(routeId).map {
            it.map(::mapRoutePointDomainToModel)
        }
    }

    fun deletePoint(point: RoutePointModel) {
        viewModelScope.launch {
            deletePointUseCase.invoke(mapRoutePointModelToPointDetailsDomain(point))
        }
    }

    fun deleteRoute(route: RouteModel) {
        viewModelScope.launch {
            deleteRouteUseCase.invoke(mapRouteModelToDomain(route))
        }
    }

    fun changeRouteAccess(routeId: String, isPublic: Boolean) {
        viewModelScope.launch {
            changeRouteAccessUseCase.invoke(routeId, isPublic)
        }
    }
}