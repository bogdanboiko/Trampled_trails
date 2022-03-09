package com.example.gh_coursework.ui.private_point

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.AddPointPreviewUseCase
import com.example.gh_coursework.domain.usecase.GetPointsPreviewUseCase
import com.example.gh_coursework.ui.private_point.mapper.mapPointDomainToModel
import com.example.gh_coursework.ui.private_point.mapper.mapPointModelToDomain
import com.example.gh_coursework.ui.private_point.model.PrivatePointModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PointViewModel(private val addPointPreviewUseCase: AddPointPreviewUseCase,
                     private val getPointsPreviewUseCase: GetPointsPreviewUseCase) : ViewModel() {
    val points = getPointsPreviewUseCase.invoke().map { pointList -> pointList.map(::mapPointDomainToModel) }

    fun addPoint(point: PrivatePointModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointPreviewUseCase.invoke(mapPointModelToDomain(point))
        }
    }
}