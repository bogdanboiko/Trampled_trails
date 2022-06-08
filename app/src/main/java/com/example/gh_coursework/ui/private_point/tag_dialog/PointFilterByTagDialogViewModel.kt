package com.example.gh_coursework.ui.private_point.tag_dialog

import androidx.lifecycle.ViewModel
import com.example.gh_coursework.domain.usecase.point_tag.GetPointTagListUseCase
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import kotlinx.coroutines.flow.map

class PointFilterByTagDialogViewModel(
    getPointTagListUseCase: GetPointTagListUseCase
) : ViewModel() {
    val tags =
        getPointTagListUseCase.invoke().map { tagList -> tagList.map(::mapPointTagDomainToModel) }
}