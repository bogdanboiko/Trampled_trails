package com.example.gh_coursework.ui.private_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.*
import com.example.gh_coursework.ui.private_route.mapper.*
import com.example.gh_coursework.ui.private_route.model.PrivateRouteModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointDetailsPreviewModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteViewModel(
    getPointsPreviewUseCase: GetPointsPreviewUseCase,
    getRoutesListUseCase: GetRoutesListUseCase,
    private val addPointPreviewUseCase: AddPointPreviewUseCase,
    private val addRouteUseCase: AddRouteUseCase,
    private val getPointDetailsUseCase: GetPointDetailsUseCase,
    private val deletePointUseCase: DeletePointUseCase
) : ViewModel() {

    val points = getPointsPreviewUseCase.invoke()
            .map { pointList -> pointList.map(::mapPointDomainToModel) }
    val routes = getRoutesListUseCase.invoke()
            .map { route -> route.map(::mapRouteDomainToModel) }

    fun addPoint(point: PrivateRoutePointModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointPreviewUseCase.invoke(mapPointModelToDomain(point))
        }
    }

    fun addRoute(route: PrivateRouteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addRouteUseCase.invoke(mapRouteModelToDomainMapper(route))
        }
    }

    fun getPointDetailsPreview(pointId: Int): Flow<PrivateRoutePointDetailsPreviewModel?> {
        return getPointDetailsUseCase.invoke(pointId)
            .map { details -> mapRoutePointDetailsDomainToModel(details) }
    }

    fun deletePoint(pointId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePointUseCase.invoke(pointId)
        }
    }
}