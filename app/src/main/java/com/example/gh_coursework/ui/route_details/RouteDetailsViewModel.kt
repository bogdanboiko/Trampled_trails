package com.example.gh_coursework.ui.route_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.route_details.GetRouteDetailsUseCase
import com.example.gh_coursework.domain.usecase.route_details.UpdateRouteDetailsUseCase
import com.example.gh_coursework.ui.route_details.mapper.mapRouteDetailsDomainToModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteDetailsModelToDomain
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteDetailsViewModel(
    routeId: Long,
    getRouteDetailsUseCase: GetRouteDetailsUseCase,
    private val updateRouteDetailsUseCase: UpdateRouteDetailsUseCase,
) : ViewModel() {

    val route = getRouteDetailsUseCase.invoke(routeId).map(::mapRouteDetailsDomainToModel)

    fun updateRouteDetails(route: RouteDetailsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            updateRouteDetailsUseCase.invoke(mapRouteDetailsModelToDomain(route))
        }
    }
}