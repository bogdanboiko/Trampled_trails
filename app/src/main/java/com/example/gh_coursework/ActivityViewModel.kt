package com.example.gh_coursework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.deleted.DeleteAllUseCase
import com.example.gh_coursework.domain.usecase.deleted.DeleteRemoteRouteUseCase
import com.example.gh_coursework.domain.usecase.deleted.GetDeletedPointsUseCase
import com.example.gh_coursework.domain.usecase.deleted.GetDeletedRoutesUseCase
import com.example.gh_coursework.domain.usecase.public.FetchRoutePointsFromRemoteUseCase
import com.example.gh_coursework.domain.usecase.public.GetUserRouteListUseCase
import com.example.gh_coursework.domain.usecase.public.PublishRouteUseCase
import com.example.gh_coursework.domain.usecase.public.SavePublicRouteToPrivateUseCase
import com.example.gh_coursework.domain.usecase.route_points.GetRoutePointsListUseCase
import com.example.gh_coursework.domain.usecase.route_preview.GetRoutesListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ActivityViewModel(
    private val deleteAllUseCase: DeleteAllUseCase,
    private val deleteRemoteRouteUseCase: DeleteRemoteRouteUseCase,
    private val getDeletedRoutesUseCase: GetDeletedRoutesUseCase,
    private val getDeletedPointsUseCase: GetDeletedPointsUseCase,
    private val getRoutesListUseCase: GetRoutesListUseCase,
    private val getRoutePointsListUseCase: GetRoutePointsListUseCase,
    private val getUserRouteListUseCase: GetUserRouteListUseCase,
    private val publishRouteUseCase: PublishRouteUseCase,
    private val fetchRoutePointsFromRemoteUseCase: FetchRoutePointsFromRemoteUseCase,
    private val savePublicRouteToPrivateUseCase: SavePublicRouteToPrivateUseCase
) : ViewModel() {

    val deletedRoutes = getDeletedRoutesUseCase.invoke()
    val deletedPoints = getDeletedPointsUseCase.invoke()

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAllUseCase.invoke()
        }
    }

    fun deleteRemoteRoute(routeId: String) {
        viewModelScope.launch {
            deleteRemoteRouteUseCase.invoke(routeId)
        }
    }

    fun uploadActualRoutesToFirebase(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                getRoutesListUseCase.invoke().first().forEach { route ->
                    publishRouteUseCase.invoke(
                        route,
                        getRoutePointsListUseCase.invoke(route.routeId).first(),
                        id
                    )
                }
            }.invokeOnCompletion {
                viewModelScope.launch(Dispatchers.IO) {
                    getUserRouteListUseCase.invoke(id).collect { routesToSave ->
                        routesToSave.forEach { route ->
                            viewModelScope.launch(Dispatchers.IO) {
                                fetchRoutePointsFromRemoteUseCase.invoke(route.routeId)
                                    .collect { routePoints ->
                                        savePublicRouteToPrivateUseCase.invoke(
                                            route,
                                            routePoints
                                        )
                                    }
                            }
                        }
                    }
                }
            }
        }
    }
}