package com.example.trampled_trails.ui.route_details.image_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trampled_trails.domain.usecase.image.DeletePointImageUseCase
import com.example.trampled_trails.domain.usecase.image.DeleteRouteImageUseCase
import com.example.trampled_trails.domain.usecase.image.GetRouteImagesUseCase
import com.example.trampled_trails.domain.usecase.route_details.GetRoutePointsImagesUseCase
import com.example.trampled_trails.ui.point_details.mapper.mapPointImageModelToDomain
import com.example.trampled_trails.ui.private_image_details.model.ImageModel.PointImageModel
import com.example.trampled_trails.ui.private_image_details.model.ImageModel.RouteImageModel
import com.example.trampled_trails.ui.route_details.mapper.mapRouteImageDomainToModel
import com.example.trampled_trails.ui.route_details.mapper.mapRouteImageModelToDomain
import com.example.trampled_trails.ui.route_details.mapper.mapRoutePointsImagesDomainToModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteDetailsImageViewModel(
    routeId: String,
    getRouteImagesUseCase: GetRouteImagesUseCase,
    getRoutePointsImagesUseCase: GetRoutePointsImagesUseCase,
    private val deleteRouteImageUseCase: DeleteRouteImageUseCase,
    private val deletePointImageUseCase: DeletePointImageUseCase
) : ViewModel() {

    val routeImages =
        getRouteImagesUseCase.invoke(routeId).map { it.map(::mapRouteImageDomainToModel) }

    val pointImages =
        getRoutePointsImagesUseCase.invoke(routeId).map { it.map(::mapRoutePointsImagesDomainToModel) }


    fun deleteRouteImage(image: RouteImageModel) {
        viewModelScope.launch {
            deleteRouteImageUseCase.invoke(mapRouteImageModelToDomain(image))
        }
    }

    fun deletePointImage(image: PointImageModel) {
        viewModelScope.launch {
            deletePointImageUseCase.invoke(mapPointImageModelToDomain(image))
        }
    }
}