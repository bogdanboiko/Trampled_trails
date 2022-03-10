package com.example.gh_coursework.ui.private_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.AddPointPreviewUseCase
import com.example.gh_coursework.domain.usecase.DeletePointUseCase
import com.example.gh_coursework.domain.usecase.GetPointDetailsUseCase
import com.example.gh_coursework.domain.usecase.GetPointsPreviewUseCase
import com.example.gh_coursework.ui.private_route.mapper.mapRoutePointDetailsDomainToModel
import com.example.gh_coursework.ui.private_route.mapper.mapRoutePointDomainToModel
import com.example.gh_coursework.ui.private_route.mapper.mapRoutePointModelToDomain
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointDetailsPreviewModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteViewModel(
    private val addPointPreviewUseCase: AddPointPreviewUseCase,
    private val getPointsPreviewUseCase: GetPointsPreviewUseCase,
    private val getPointDetailsUseCase: GetPointDetailsUseCase,
    private val deletePointUseCase: DeletePointUseCase
) : ViewModel() {
    val points =
        getPointsPreviewUseCase.invoke().map { pointList -> pointList.map(::mapRoutePointDomainToModel) }

    fun addPoint(point: PrivateRoutePointModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointPreviewUseCase.invoke(mapRoutePointModelToDomain(point))
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