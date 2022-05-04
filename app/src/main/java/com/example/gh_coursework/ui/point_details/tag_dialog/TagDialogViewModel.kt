package com.example.gh_coursework.ui.point_details.tag_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.point_tag.*
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagModelToDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointsTagsModelToDomain
import com.example.gh_coursework.ui.point_details.model.PointTagModel
import com.example.gh_coursework.ui.point_details.model.PointsTagsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TagDialogViewModel(
    private val pointId: String,
    private val getPointTagListUseCase: GetPointTagListUseCase,
    private val addPointsTagsListUseCase: AddPointsTagsListUseCase,
    private val removePointsTagsListUseCase: RemovePointsTagsListUseCase,
    private val getPointTagsUseCase: GetPointTagsUseCase
) : ViewModel() {
    val tags =
        getPointTagListUseCase.invoke().map { tagList -> tagList.map(::mapPointTagDomainToModel) }

    val pointTags = getPointTagsUseCase.invoke(pointId).map { it.map(::mapPointTagDomainToModel) }

    fun addTagsToPoint(tags: List<PointsTagsModel>) {
        viewModelScope.launch {
            addPointsTagsListUseCase.invoke(tags.map(::mapPointsTagsModelToDomain))
        }
    }

    fun removeTagsToPoint(tags: List<PointsTagsModel>) {
        viewModelScope.launch {
            removePointsTagsListUseCase.invoke(tags.map(::mapPointsTagsModelToDomain))
        }
    }
}