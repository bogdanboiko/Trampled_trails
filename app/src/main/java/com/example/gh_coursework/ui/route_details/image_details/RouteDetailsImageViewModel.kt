package com.example.gh_coursework.ui.route_details.image_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.data.database.mapper.images.mapPointImageEntityToDomain
import com.example.gh_coursework.domain.usecase.image.DeletePointImageUseCase
import com.example.gh_coursework.domain.usecase.image.DeleteRouteImageUseCase
import com.example.gh_coursework.domain.usecase.image.GetRouteImagesUseCase
import com.example.gh_coursework.domain.usecase.route_details.GetRoutePointsImagesUseCase
import com.example.gh_coursework.ui.model.ImageModel.PointImageModel
import com.example.gh_coursework.ui.model.ImageModel.RouteImageModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageModelToDomain
import com.example.gh_coursework.ui.route_details.mapper.mapRouteImageDomainToModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteImageModelToDomain
import com.example.gh_coursework.ui.route_details.mapper.mapRoutePointsImagesDomainToModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteDetailsImageViewModel(
    routeId: Long,
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
        viewModelScope.launch(Dispatchers.IO) {
            deleteRouteImageUseCase.invoke(mapRouteImageModelToDomain(image))
        }
    }

    fun deletePointImage(image: PointImageModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePointImageUseCase.invoke(mapPointImageModelToDomain(image))
        }
    }
}