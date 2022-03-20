package com.example.gh_coursework.ui.route_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.route.AddRouteUseCase
import com.example.gh_coursework.domain.usecase.route.GetRoutesListUseCase
import com.example.gh_coursework.ui.private_route.mapper.mapRouteDomainToModel
import com.example.gh_coursework.ui.route_details.mapper.mapRouteDetailsModelToDomain
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RouteDetailsViewModel(
    private val routeId: Long,
    getRoutesListUseCase: GetRoutesListUseCase,
    private val addRouteUseCase: AddRouteUseCase,
) : ViewModel() {

    val route = getRoutesListUseCase.invoke().map { route ->
        route.map(::mapRouteDomainToModel).find {
            it.routeId == routeId
        }
    }

    fun addRoute(route: RouteDetailsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addRouteUseCase.invoke(mapRouteDetailsModelToDomain(route))
        }
    }
}