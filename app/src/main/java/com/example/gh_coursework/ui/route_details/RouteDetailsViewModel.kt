package com.example.gh_coursework.ui.route_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.image.AddRouteImageListUseCase
import com.example.gh_coursework.domain.usecase.route_details.GetRouteDetailsUseCase
import com.example.gh_coursework.domain.usecase.route_details.GetRoutePointsImagesUseCase
import com.example.gh_coursework.domain.usecase.route_details.UpdateRouteDetailsUseCase
import com.example.gh_coursework.ui.private_route.mapper.mapRouteImageModelToPointImageModel
import com.example.gh_coursework.ui.route_details.mapper.*
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel
import com.example.gh_coursework.ui.route_details.model.RouteImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteDetailsViewModel(
    routeId: Long,
    getRouteDetailsUseCase: GetRouteDetailsUseCase,
    getRoutePointsImagesUseCase: GetRoutePointsImagesUseCase,
    private val updateRouteDetailsUseCase: UpdateRouteDetailsUseCase,
    private val addRouteImageListUseCase: AddRouteImageListUseCase
) : ViewModel() {

    val route = getRouteDetailsUseCase.invoke(routeId).map(::mapRouteDetailsDomainToModel)
    val images = getRoutePointsImagesUseCase.invoke(routeId).map {
        image -> image.map(::mapRoutePointsImagesDomainToModel)
    }

    fun updateRouteDetails(route: RouteDetailsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            updateRouteDetailsUseCase.invoke(mapRouteDetailsModelToDomain(route))
        }
    }

    fun addPointImageList(images: List<RouteImageModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            addRouteImageListUseCase.invoke(images.map(::mapRouteImageModelToDomain))
        }
    }
}