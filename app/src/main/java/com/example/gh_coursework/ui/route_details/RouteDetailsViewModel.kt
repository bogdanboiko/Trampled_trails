package com.example.gh_coursework.ui.route_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.image.AddRouteImageListUseCase
import com.example.gh_coursework.domain.usecase.route_details.GetRouteDetailsUseCase
import com.example.gh_coursework.domain.usecase.route_details.UpdateRouteDetailsUseCase
import com.example.gh_coursework.ui.route_details.mapper.mapRouteDetailsDomainToModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteDetailsModelToDomain
import com.example.gh_coursework.ui.route_details.mapper.mapRouteImageModelToDomain
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel
import com.example.gh_coursework.ui.route_details.model.RouteImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteDetailsViewModel(
    routeId: Long,
    getRouteDetailsUseCase: GetRouteDetailsUseCase,
    private val updateRouteDetailsUseCase: UpdateRouteDetailsUseCase,
    private val addRouteImageListUseCase: AddRouteImageListUseCase
) : ViewModel() {

    val route = getRouteDetailsUseCase.invoke(routeId).map(::mapRouteDetailsDomainToModel)

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