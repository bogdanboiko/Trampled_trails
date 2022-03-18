package com.example.gh_coursework.ui.private_point

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.point_preview.AddPointPreviewUseCase
import com.example.gh_coursework.domain.usecase.point_preview.DeletePointUseCase
import com.example.gh_coursework.domain.usecase.point_details.GetPointDetailsUseCase
import com.example.gh_coursework.domain.usecase.point_preview.GetPointsPreviewUseCase
import com.example.gh_coursework.ui.private_point.mapper.mapPointDetailsDomainToModel
import com.example.gh_coursework.ui.private_point.mapper.mapPointDomainToModel
import com.example.gh_coursework.ui.private_point.mapper.mapPointModelToDomain
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsPreviewModel
import com.example.gh_coursework.ui.private_point.model.PrivatePointModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PointViewModel(
    private val addPointPreviewUseCase: AddPointPreviewUseCase,
    private val getPointsPreviewUseCase: GetPointsPreviewUseCase,
    private val getPointDetailsUseCase: GetPointDetailsUseCase,
    private val deletePointUseCase: DeletePointUseCase
) : ViewModel() {
    val points =
        getPointsPreviewUseCase.invoke().map { pointList -> pointList.map(::mapPointDomainToModel) }

    fun addPoint(point: PrivatePointModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointPreviewUseCase.invoke(mapPointModelToDomain(point))
        }
    }

    fun getPointDetailsPreview(pointId: Long): Flow<PrivatePointDetailsPreviewModel?> {
        return getPointDetailsUseCase.invoke(pointId).map { details -> mapPointDetailsDomainToModel(details) }
    }

    fun deletePoint(pointId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePointUseCase.invoke(pointId)
        }
    }
}