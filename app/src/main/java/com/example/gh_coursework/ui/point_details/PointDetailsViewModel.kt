package com.example.gh_coursework.ui.point_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.AddPointDetailsUseCase
import com.example.gh_coursework.domain.usecase.GetPointDetailsUseCase
import com.example.gh_coursework.ui.point_details.model.PointDetailsModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointDetailsDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointDetailsModelToDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PointDetailsViewModel(
    private val pointId: Int,
    private val addPointDetailsUseCase: AddPointDetailsUseCase,
    private val getPointDetailsUseCase: GetPointDetailsUseCase
) : ViewModel() {
    val pointDetails = getPointDetailsUseCase.invoke(pointId).map { mapPointDetailsDomainToModel(it) }

    fun addPointDetails(details: PointDetailsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointDetailsUseCase.invoke(mapPointDetailsModelToDomain(details))
        }
    }
}