package com.example.gh_coursework.ui.route_details.image_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.image.DeleteRouteImageUseCase
import com.example.gh_coursework.domain.usecase.image.GetRouteImagesUseCase
import com.example.gh_coursework.ui.route_details.mapper.mapRouteImageDomainToModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteImageModelToDomain
import com.example.gh_coursework.ui.route_details.model.RouteImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteDetailsImageViewModel(
    routeId: Long,
    getRouteImagesUseCase: GetRouteImagesUseCase,
    private val deleteRouteImageUseCase: DeleteRouteImageUseCase
) : ViewModel() {

    val routeImages =
        getRouteImagesUseCase.invoke(routeId).map { it.map(::mapRouteImageDomainToModel) }

    fun deleteRouteImage(image: RouteImageModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteRouteImageUseCase.invoke(mapRouteImageModelToDomain(image))
        }
    }
}