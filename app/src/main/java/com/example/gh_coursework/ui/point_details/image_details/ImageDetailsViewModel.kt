package com.example.gh_coursework.ui.point_details.image_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.image.DeletePointImageUseCase
import com.example.gh_coursework.domain.usecase.image.GetPointImagesUseCase
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointImageModelToDomain
import com.example.gh_coursework.ui.point_details.model.PointImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ImageDetailsViewModel(
    private val pointId: Long,
    private val deletePointImageUseCase: DeletePointImageUseCase,
    private val getPointImagesUseCase: GetPointImagesUseCase
) : ViewModel() {
    val pointImages = getPointImagesUseCase.invoke(pointId).map { it.map(::mapPointImageDomainToModel) }

    fun deletePointImage(image: PointImageModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePointImageUseCase.invoke(mapPointImageModelToDomain(image))
        }
    }
}