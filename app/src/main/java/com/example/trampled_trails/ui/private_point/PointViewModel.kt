package com.example.trampled_trails.ui.private_point

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trampled_trails.domain.usecase.point_preview.AddPointPreviewWithDetailsUseCase
import com.example.trampled_trails.domain.usecase.point_preview.DeletePointUseCase
import com.example.trampled_trails.domain.usecase.point_preview.GetAllPointsUseCase
import com.example.trampled_trails.ui.private_point.mapper.mapPointDomainToModel
import com.example.trampled_trails.ui.private_point.mapper.mapPointModelToDomain
import com.example.trampled_trails.ui.private_point.mapper.mapPrivatePointDetailsModelToPointDomain
import com.example.trampled_trails.ui.private_point.model.PrivatePointDetailsModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PointViewModel(
    private val addPointPreviewWithDetailsUseCase: AddPointPreviewWithDetailsUseCase,
    private val getAllPointsDetailsDetailsUseCase: GetAllPointsUseCase,
    private val deletePointUseCase: DeletePointUseCase
) : ViewModel() {
    val points =
        getAllPointsDetailsDetailsUseCase.invoke().map { pointList -> pointList.map(::mapPointDomainToModel) }

    fun addPoint(point: PrivatePointDetailsModel) {
        viewModelScope.launch {
            addPointPreviewWithDetailsUseCase.invoke(mapPointModelToDomain(point))
        }
    }

    fun deletePoint(point: PrivatePointDetailsModel) {
        viewModelScope.launch {
            deletePointUseCase.invoke(mapPrivatePointDetailsModelToPointDomain(point))
        }
    }
}