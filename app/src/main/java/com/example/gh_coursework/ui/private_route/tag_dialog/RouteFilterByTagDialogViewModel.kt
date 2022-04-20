package com.example.gh_coursework.ui.private_route.tag_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.route_details.GetRouteDetailsUseCase
import com.example.gh_coursework.domain.usecase.route_tag.AddRouteTagsUseCase
import com.example.gh_coursework.domain.usecase.route_tag.DeleteTagsFromRouteUseCase
import com.example.gh_coursework.domain.usecase.route_tag.GetRouteTagsUseCase
import com.example.gh_coursework.ui.route_details.mapper.mapRouteDetailsDomainToModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteTagDomainToModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteTagsModelToDomain
import com.example.gh_coursework.ui.route_details.model.RouteTagsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteFilterByTagDialogViewModel(
    getRouteTagsUseCase: GetRouteTagsUseCase,
) : ViewModel() {
    val tags =
        getRouteTagsUseCase.invoke().map { tagList -> tagList.map(::mapRouteTagDomainToModel) }
}