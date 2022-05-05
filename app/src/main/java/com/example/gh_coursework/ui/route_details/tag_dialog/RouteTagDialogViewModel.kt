package com.example.gh_coursework.ui.route_details.tag_dialog

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteTagDialogViewModel(
    routeId: String,
    getRouteTagsUseCase: GetRouteTagsUseCase,
    getRouteDetailsUseCase: GetRouteDetailsUseCase,
    private val addRouteTagsUseCase: AddRouteTagsUseCase,
    private val deleteTagsFromRouteUseCase: DeleteTagsFromRouteUseCase
) : ViewModel() {

    val route =
        getRouteDetailsUseCase.invoke(routeId).map(::mapRouteDetailsDomainToModel)

    val tags =
        getRouteTagsUseCase.invoke().map { tagList -> tagList.map(::mapRouteTagDomainToModel) }

    fun addTagsToRoute(tags: List<RouteTagsModel>) {
        viewModelScope.launch {
            addRouteTagsUseCase.invoke(tags.map(::mapRouteTagsModelToDomain))
        }
    }

    fun deleteTagsFromRoute(tags: List<RouteTagsModel>) {
        viewModelScope.launch {
            deleteTagsFromRouteUseCase.invoke(tags.map(::mapRouteTagsModelToDomain))
        }
    }
}