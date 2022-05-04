package com.example.gh_coursework.ui.point_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.point_details.AddPointDetailsUseCase
import com.example.gh_coursework.domain.usecase.image.AddPointImageListUseCase
import com.example.gh_coursework.domain.usecase.point_details.GetPointDetailsUseCase
import com.example.gh_coursework.ui.private_image_details.model.ImageModel.PointImageModel
import com.example.gh_coursework.ui.point_details.mapper.*
import com.example.gh_coursework.ui.point_details.model.PointDetailsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PointDetailsViewModel(
    private val pointId: String,
    private val addPointDetailsUseCase: AddPointDetailsUseCase,
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