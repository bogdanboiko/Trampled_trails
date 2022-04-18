package com.example.gh_coursework.ui.private_point.tag_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.point_tag.AddPointTagUseCase
import com.example.gh_coursework.domain.usecase.point_tag.GetPointTagListUseCase
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagModelToDomain
import com.example.gh_coursework.ui.point_details.model.PointTagModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PointFilterByTagDialogViewModel(
    getPointTagListUseCase: GetPointTagListUseCase,
    private val addPointTagUseCase: AddPointTagUseCase
) : ViewModel() {
    val tags =
        getPointTagListUseCase.invoke().map { tagList -> tagList.map(::mapPointTagDomainToModel) }

    fun addTag(tag: PointTagModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointTagUseCase.invoke(mapPointTagModelToDomain(tag))
        }
    }
}