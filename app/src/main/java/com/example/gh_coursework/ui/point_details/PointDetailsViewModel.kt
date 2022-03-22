package com.example.gh_coursework.ui.point_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.point_details.AddPointDetailsUseCase
import com.example.gh_coursework.domain.usecase.point_details.GetPointDetailsUseCase
import com.example.gh_coursework.ui.point_details.mapper.mapPointDetailsDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointDetailsModelToDomain
import com.example.gh_coursework.ui.point_details.model.PointDetailsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PointDetailsViewModel(
    private val pointId: Long,
    private val addPointDetailsUseCase: AddPointDetailsUseCase,
    private val getPointDetailsUseCase: GetPointDetailsUseCase
) : ViewModel() {
    val pointDetails =
        getPointDetailsUseCase.invoke(pointId).map { mapPointDetailsDomainToModel(it) }

    fun addPointDetails(details: PointDetailsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointDetailsUseCase.invoke(mapPointDetailsModelToDomain(details))
        }
    }
}