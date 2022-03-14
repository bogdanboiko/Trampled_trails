package com.example.gh_coursework.ui.point_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.*
import com.example.gh_coursework.ui.point_details.mapper.*
import com.example.gh_coursework.ui.point_details.model.PointDetailsModel
import com.example.gh_coursework.ui.point_details.model.PointTagModel
import com.example.gh_coursework.ui.point_details.model.PointsTagsModel
import com.example.gh_coursework.ui.private_point.mapper.mapPointModelToDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class PointDetailsViewModel(
    private val pointId: Int,
    private val addPointDetailsUseCase: AddPointDetailsUseCase,
    private val getPointDetailsUseCase: GetPointDetailsUseCase,
    private val addPointTagUseCase: AddPointTagUseCase,
    private val getPointTagListUseCase: GetPointTagListUseCase,
    private val addPointsTagsListUseCase: AddPointsTagsListUseCase,
    private val removePointsTagsListUseCase: RemovePointsTagsListUseCase,
    private val deletePointTagUseCase: DeletePointTagUseCase
) : ViewModel() {
    val pointDetails = getPointDetailsUseCase.invoke(pointId).map { mapPointDetailsDomainToModel(it) }
    val tags = getPointTagListUseCase.invoke().map { tagList -> tagList.map(::mapPointTagDomainToModel) }

    fun addTag(tag: PointTagModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointTagUseCase.invoke(mapPointTagModelToDomain(tag))
        }
    }

    fun addPointDetails(details: PointDetailsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointDetailsUseCase.invoke(mapPointDetailsModelToDomain(details))
        }
    }

    fun addTagsToPoint(tags: List<PointsTagsModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointsTagsListUseCase.invoke(tags.map(::mapPointsTagsModelToDomain))
        }
    }

    fun removeTagsToPoint(tags: List<PointsTagsModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            removePointsTagsListUseCase.invoke(tags.map(::mapPointsTagsModelToDomain))
        }
    }

    fun deletePointTag(tag: PointTagModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePointTagUseCase.invoke(mapPointTagModelToDomain(tag))
        }
    }
}