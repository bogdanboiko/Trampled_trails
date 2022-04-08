package com.example.gh_coursework.ui.private_route

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.image.AddRouteImageListUseCase
import com.example.gh_coursework.domain.usecase.point_preview.DeletePointUseCase
import com.example.gh_coursework.domain.usecase.public.PublishRouteUseCase
import com.example.gh_coursework.domain.usecase.route_points.GetRoutePointsListUseCase
import com.example.gh_coursework.domain.usecase.route_preview.AddRouteUseCase
import com.example.gh_coursework.domain.usecase.route_preview.DeleteRouteUseCase
import com.example.gh_coursework.domain.usecase.route_preview.GetRoutesListUseCase
import com.example.gh_coursework.ui.model.ImageModel.RouteImageModel
import com.example.gh_coursework.ui.private_route.mapper.mapRouteDomainToModel
import com.example.gh_coursework.ui.private_route.mapper.mapRouteModelToDomain
import com.example.gh_coursework.ui.private_route.mapper.mapRoutePointDomainToModel
import com.example.gh_coursework.ui.private_route.mapper.mapRoutePointModelToDomain
import com.example.gh_coursework.ui.private_route.model.RouteModel
import com.example.gh_coursework.ui.private_route.model.RoutePointModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteImageModelToDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteViewModel(
    getRoutesListUseCase: GetRoutesListUseCase,
    private val addRouteUseCase: AddRouteUseCase,
    private val addRouteImageListUseCase: AddRouteImageListUseCase,
    private val getRoutePointsListUseCase: GetRoutePointsListUseCase,
    private val deletePointUseCase: DeletePointUseCase,
    private val deleteRouteUseCase: DeleteRouteUseCase,
    private val publishRouteUseCase: PublishRouteUseCase
) : ViewModel() {

    val routes = getRoutesListUseCase.invoke()
        .map { route -> route.map(::mapRouteDomainToModel) }

    fun addRoute(route: RouteModel, pointsList: List<RoutePointModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            addRouteUseCase.invoke(
                mapRouteModelToDomain(route),
                pointsList.map(::mapRoutePointModelToDomain)
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
            deleteRouteUseCase.invoke(mapRouteModelToDomain(route))
        }
    }

    fun publishRoute(route: RouteModel, routePoints: List<RoutePointModel>) {
        Log.e("e", routePoints.map(::mapRoutePointModelToDomain).toString())
        Log.e("e", mapRouteModelToDomain(route).toString())
        publishRouteUseCase.invoke(mapRouteModelToDomain(route), routePoints.map(::mapRoutePointModelToDomain))
    }
}