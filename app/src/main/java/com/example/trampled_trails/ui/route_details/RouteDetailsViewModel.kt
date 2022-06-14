package com.example.trampled_trails.ui.route_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trampled_trails.domain.usecase.image.AddRouteImageListUseCase
import com.example.trampled_trails.domain.usecase.route_details.GetRouteDetailsUseCase
import com.example.trampled_trails.domain.usecase.route_details.GetRoutePointsImagesUseCase
import com.example.trampled_trails.domain.usecase.route_details.UpdateRouteDetailsUseCase
import com.example.trampled_trails.ui.private_image_details.model.ImageModel.RouteImageModel
import com.example.trampled_trails.ui.route_details.mapper.mapRouteDetailsDomainToModel
import com.example.trampled_trails.ui.route_details.mapper.mapRouteDetailsModelToDomain
import com.example.trampled_trails.ui.route_details.mapper.mapRouteImageModelToDomain
import com.example.trampled_trails.ui.route_details.mapper.mapRoutePointsImagesDomainToModel
import com.example.trampled_trails.ui.route_details.model.RouteDetailsModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteDetailsViewModel(
    routeId: String,
    getRouteDetailsUseCase: GetRouteDetailsUseCase,
    getRoutePointsImagesUseCase: GetRoutePointsImagesUseCase,
    private val updateRouteDetailsUseCase: UpdateRouteDetailsUseCase,
    private val addRouteImageListUseCase: AddRouteImageListUseCase
) : ViewModel() {

    val route = getRouteDetailsUseCase.invoke(routeId).map(::mapRouteDetailsDomainToModel)
    val images = getRoutePointsImagesUseCase.invoke(routeId).map { image ->
        image.map(::mapRoutePointsImagesDomainToModel)
    }

    fun updateRouteDetails(route: RouteDetailsModel) {
        viewModelScope.launch {
            updateRouteDetailsUseCase.invoke(mapRouteDetailsModelToDomain(route))
        }
    }

    fun addPointImageList(images: List<RouteImageModel>) {
        viewModelScope.launch {
            addRouteImageListUseCase.invoke(images.map(::mapRouteImageModelToDomain))
        }
    }
}