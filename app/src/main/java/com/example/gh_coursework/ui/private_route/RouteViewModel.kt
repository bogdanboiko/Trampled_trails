package com.example.gh_coursework.ui.private_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.image.AddRouteImageListUseCase
import com.example.gh_coursework.domain.usecase.image.GetRouteImagesUseCase
import com.example.gh_coursework.domain.usecase.point_preview.DeletePointUseCase
import com.example.gh_coursework.domain.usecase.route_points.GetRoutePointsListUseCase
import com.example.gh_coursework.domain.usecase.route_preview.AddRouteUseCase
import com.example.gh_coursework.domain.usecase.route_preview.DeleteRouteUseCase
import com.example.gh_coursework.domain.usecase.route_preview.GetRoutesListUseCase
import com.example.gh_coursework.ui.private_route.mapper.mapRouteDomainToModel
import com.example.gh_coursework.ui.private_route.mapper.mapRouteModelToDomainMapper
import com.example.gh_coursework.ui.private_route.mapper.mapRoutePointDomainToModel
import com.example.gh_coursework.ui.private_route.mapper.mapRoutePointModelToDomainMapper
import com.example.gh_coursework.ui.private_route.model.RouteModel
import com.example.gh_coursework.ui.private_route.model.RoutePointModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteImageDomainToModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteImageModelToDomain
import com.example.gh_coursework.ui.route_details.model.RouteImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteViewModel(
    getRoutesListUseCase: GetRoutesListUseCase,
    private val addRouteUseCase: AddRouteUseCase,
    private val addRouteImageListUseCase: AddRouteImageListUseCase,
    private val getRoutePointsListUseCase: GetRoutePointsListUseCase,
    private val getRouteImagesUseCase: GetRouteImagesUseCase,
    private val deletePointUseCase: DeletePointUseCase,
    private val deleteRouteUseCase: DeleteRouteUseCase
) : ViewModel() {

    val routes = getRoutesListUseCase.invoke()
        .map { route -> route.map(::mapRouteDomainToModel) }

    fun addRoute(route: RouteModel, pointsList: List<RoutePointModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            addRouteUseCase.invoke(
                mapRouteModelToDomainMapper(route),
                pointsList.map(::mapRoutePointModelToDomainMapper)
            )
        }
    }

    fun addRouteImageList(images: List<RouteImageModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            addRouteImageListUseCase.invoke(images.map(::mapRouteImageModelToDomain))
        }
    }

    fun getRoutePointsList(routeId: Long): Flow<List<RoutePointModel>> {
        return getRoutePointsListUseCase.invoke(routeId).map {
            it.map(::mapRoutePointDomainToModel)
        }
    }

    fun deletePoint(pointId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePointUseCase.invoke(pointId)
        }
    }

    fun deleteRoute(route: RouteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteRouteUseCase.invoke(mapRouteModelToDomainMapper(route))
        }
    }
}