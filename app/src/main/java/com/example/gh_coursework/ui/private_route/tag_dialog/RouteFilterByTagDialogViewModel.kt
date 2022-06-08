package com.example.gh_coursework.ui.private_route.tag_dialog

import androidx.lifecycle.ViewModel
import com.example.gh_coursework.domain.usecase.route_tag.GetRouteTagsUseCase
import com.example.gh_coursework.ui.route_details.mapper.mapRouteTagDomainToModel
import kotlinx.coroutines.flow.map

class RouteFilterByTagDialogViewModel(
    getRouteTagsUseCase: GetRouteTagsUseCase,
) : ViewModel() {
    val tags =
        getRouteTagsUseCase.invoke().map { tagList -> tagList.map(::mapRouteTagDomainToModel) }
}