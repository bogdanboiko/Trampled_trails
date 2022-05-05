package com.example.gh_coursework.ui.private_point

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.point_preview.GetAllPointsDetailsUseCase
import com.example.gh_coursework.domain.usecase.point_preview.AddPointPreviewWithDetailsUseCase
import com.example.gh_coursework.domain.usecase.point_preview.DeletePointUseCase
import com.example.gh_coursework.ui.private_point.mapper.mapPointDomainToModel
import com.example.gh_coursework.ui.private_point.mapper.mapPointModelToDomain
import com.example.gh_coursework.ui.private_point.mapper.mapPrivatePointDetailsModelToPointDomain
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsModel
import com.example.gh_coursework.ui.private_point.model.PrivatePointPreviewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PointViewModel(
    private val addPointPreviewWithDetailsUseCase: AddPointPreviewWithDetailsUseCase,
    private val getAllPointsDetailsDetailsUseCase: GetAllPointsDetailsUseCase,
    private val deletePointUseCase: DeletePointUseCase
) : ViewModel() {
    val points =
        getAllPointsDetailsDetailsUseCase.invoke().map { pointList -> pointList.map(::mapPointDomainToModel) }

    fun addPoint(point: PrivatePointPreviewModel) {
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