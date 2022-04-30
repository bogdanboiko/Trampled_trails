package com.example.gh_coursework.ui.private_point

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.point_details.GetAllPointsDetailsUseCase
import com.example.gh_coursework.domain.usecase.point_preview.AddPointPreviewWithDetailsUseCase
import com.example.gh_coursework.domain.usecase.point_preview.DeletePointUseCase
import com.example.gh_coursework.ui.private_point.mapper.mapPointDomainToModel
import com.example.gh_coursework.ui.private_point.mapper.mapPointModelToDomain
import com.example.gh_coursework.ui.private_point.model.PrivatePointModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PointViewModel(
    private val addPointPreviewWithDetailsUseCase: AddPointPreviewWithDetailsUseCase,
    private val getAllPointsDetailsDetailsUseCase: GetAllPointsDetailsUseCase,
    private val deletePointUseCase: DeletePointUseCase
) : ViewModel() {
    val points =
        getAllPointsDetailsDetailsUseCase.invoke().map { pointList -> pointList.map(::mapPointDomainToModel) }

    fun addPoint(point: PrivatePointModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointPreviewWithDetailsUseCase.invoke(mapPointModelToDomain(point))
        }
    }

    fun deletePoint(pointId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePointUseCase.invoke(pointId)
        }
    }
}