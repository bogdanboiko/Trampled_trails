package com.example.gh_coursework.ui.private_image_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.image.DeletePointImageUseCase
import com.example.gh_coursework.domain.usecase.image.GetPointImagesUseCase
import com.example.gh_coursework.ui.private_image_details.model.ImageModel.PointImageModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageModelToDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ImageDetailsViewModel(
    private val pointId: String,
    private val deletePointImageUseCase: DeletePointImageUseCase,
    private val getPointImagesUseCase: GetPointImagesUseCase
) : ViewModel() {
    val pointImages = getPointImagesUseCase.invoke(pointId).map { it.map(::mapPointImageDomainToModel) }

    fun deletePointImage(image: PointImageModel) {
        viewModelScope.launch {
            deletePointImageUseCase.invoke(mapPointImageModelToDomain(image))
        }
    }
}