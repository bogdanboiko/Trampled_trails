package com.example.gh_coursework.ui.private_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.image.AddRouteImageListUseCase
import com.example.gh_coursework.domain.usecase.image.GetRouteImagesUseCase
import com.example.gh_coursework.domain.usecase.point_details.GetPointDetailsUseCase
import com.example.gh_coursework.domain.usecase.point_preview.DeletePointUseCase
import com.example.gh_coursework.domain.usecase.route_preview.AddRouteUseCase
import com.example.gh_coursework.domain.usecase.route_preview.DeleteRouteUseCase
import com.example.gh_coursework.domain.usecase.route_preview.GetRoutesListUseCase
import com.example.gh_coursework.ui.private_route.mapper.mapRouteDomainToModel
import com.example.gh_coursework.ui.private_route.mapper.mapRouteModelToDomainMapper
import com.example.gh_coursework.ui.private_route.mapper.mapRoutePointDetailsDomainToModel
import com.example.gh_coursework.ui.private_route.model.PrivateRouteModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointDetailsPreviewModel
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
    private val getPointDetailsUseCase: GetPointDetailsUseCase,
    private val getRouteImagesUseCase: GetRouteImagesUseCase,
    private val deletePointUseCase: DeletePointUseCase,
    private val deleteRouteUseCase: DeleteRouteUseCase
) : ViewModel() {

    val routes = getRoutesListUseCase.invoke()
        .map { route -> route.map(::mapRouteDomainToModel) }

    fun addRoute(route: PrivateRouteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addRouteUseCase.invoke(mapRouteModelToDomainMapper(route))
        }
    }

    fun addRouteImageList(images: List<RouteImageModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            addRouteImageListUseCase.invoke(images.map(::mapRouteImageModelToDomain))
        }
    }

    fun getPointDetailsPreview(pointId: Long): Flow<PrivateRoutePointDetailsPreviewModel?> {
        return getPointDetailsUseCase.invoke(pointId)
            .map { details -> mapRoutePointDetailsDomainToModel(details) }
    }

    fun getRouteImages(routeId: Long): Flow<List<RouteImageModel>> {
        return getRouteImagesUseCase.invoke(routeId)
            .map { image -> image.map(::mapRouteImageDomainToModel) }
    }

    fun deletePoint(pointId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePointUseCase.invoke(pointId)
        }
    }

    fun deleteRoute(route: PrivateRouteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteRouteUseCase.invoke(mapRouteModelToDomainMapper(route))
        }
    }
}