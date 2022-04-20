package com.example.gh_coursework.ui.private_point

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.point_details.GetAllPointsDetails
import com.example.gh_coursework.domain.usecase.point_preview.AddPointPreviewWithDetailsUseCase
import com.example.gh_coursework.domain.usecase.point_preview.DeletePointUseCase
import com.example.gh_coursework.ui.private_point.mapper.mapPointCompleteDetailsDomainToModel
import com.example.gh_coursework.ui.private_point.mapper.mapPointModelToDomain
import com.example.gh_coursework.ui.private_point.model.PrivatePointModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PointViewModel(
    private val addPointPreviewWithDetailsUseCase: AddPointPreviewWithDetailsUseCase,
    private val getAllPointsDetailsUseCase: GetAllPointsDetails,
    private val deletePointUseCase: DeletePointUseCase
) : ViewModel() {
    val points =
        getAllPointsDetailsUseCase.invoke().map { pointList -> pointList.map(::mapPointCompleteDetailsDomainToModel) }

    fun addPoint(point: PrivatePointModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointPreviewWithDetailsUseCase.invoke(mapPointModelToDomain(point))
        }
    }

    fun deletePoint(pointId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePointUseCase.invoke(pointId)
        }
    }
}