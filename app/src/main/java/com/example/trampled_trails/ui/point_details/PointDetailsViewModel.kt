package com.example.trampled_trails.ui.point_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trampled_trails.domain.usecase.point_details.UpdatePointDetailsUseCase
import com.example.trampled_trails.domain.usecase.point_details.AddPointImageListUseCase
import com.example.trampled_trails.domain.usecase.point_details.GetPointDetailsUseCase
import com.example.trampled_trails.ui.private_image_details.model.ImageModel.PointImageModel
import com.example.trampled_trails.ui.point_details.mapper.*
import com.example.trampled_trails.ui.point_details.model.PointDetailsModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PointDetailsViewModel(
    private val pointId: String,
    private val addPointDetailsUseCase: UpdatePointDetailsUseCase,
    private val getPointDetailsUseCase: GetPointDetailsUseCase,
    private val addPointImageListUseCase: AddPointImageListUseCase
) : ViewModel() {
    val pointDetails =
        getPointDetailsUseCase.invoke(pointId).map { mapPointDetailsDomainToModel(it) }

    fun addPointDetails(details: PointDetailsModel) {
        viewModelScope.launch {
            addPointDetailsUseCase.invoke(mapPointDetailsModelToDomain(details))
        }
    }

    fun addPointImageList(images: List<PointImageModel>) {
        viewModelScope.launch {
            addPointImageListUseCase.invoke(images.map(::mapPointImageModelToDomain))
        }
    }
}