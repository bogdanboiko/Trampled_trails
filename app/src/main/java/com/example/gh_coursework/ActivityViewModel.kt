package com.example.gh_coursework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.deleted.*
import com.example.gh_coursework.domain.usecase.public.FetchRoutePointsFromRemoteUseCase
import com.example.gh_coursework.domain.usecase.public.GetUserRouteListUseCase
import com.example.gh_coursework.domain.usecase.public.UploadRouteToFirebaseUseCase
import com.example.gh_coursework.domain.usecase.public.SavePublicRouteToPrivateUseCase
import com.example.gh_coursework.domain.usecase.route_points.GetRoutePointsListUseCase
import com.example.gh_coursework.domain.usecase.route_preview.GetRoutesListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ActivityViewModel(
    private val deleteAllUseCase: DeleteAllUseCase,
    private val deleteRemoteRouteUseCase: DeleteRemoteRouteUseCase,
    private val clearDeletedPointsTableUseCase: ClearDeletedPointsTableUseCase,
    private val clearDeletesRoutesTableUseCase: ClearDeletedRoutesTableUseCase,
    private val getDeletedRoutesUseCase: GetDeletedRoutesUseCase,
    private val getDeletedPointsUseCase: GetDeletedPointsUseCase,
    private val getRoutesListUseCase: GetRoutesListUseCase,
    private val getRoutePointsListUseCase: GetRoutePointsListUseCase,
    private val getUserRouteListUseCase: GetUserRouteListUseCase,
    private val publishRouteUseCase: UploadRouteToFirebaseUseCase,
    private val fetchRoutePointsFromRemoteUseCase: FetchRoutePointsFromRemoteUseCase,
    private val savePublicRouteToPrivateUseCase: SavePublicRouteToPrivateUseCase
) : ViewModel() {

    private val deletedRoutes = getDeletedRoutesUseCase.invoke()
    val deletedPoints = getDeletedPointsUseCase.invoke()

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAllUseCase.invoke()
        }
    }

    fun uploadActualRoutesToFirebase(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteRemoteRoute()
        }.invokeOnCompletion {
            clearDeletedRoutesTable()

            viewModelScope.launch(Dispatchers.IO) {
                viewModelScope.launch(Dispatchers.IO) {
                    publishRoutes(id)
                }.invokeOnCompletion {
                    viewModelScope.launch(Dispatchers.IO) {
                        fetchRoutes(id)
                    }
                }
            }
        }
    }

    private suspend fun deleteRemoteRoute() {
        val deletedRoutes = deletedRoutes.first()

        if (deletedRoutes.isNotEmpty()) {
            deletedRoutes.forEach {
                deleteRemoteRouteUseCase.invoke(it.routeId)
            }
        }
    }

    private fun clearDeletedRoutesTable() {
        viewModelScope.launch(Dispatchers.IO) {
            clearDeletesRoutesTableUseCase.invoke()
        }
    }

    private suspend fun publishRoutes(id: String) {
        getRoutesListUseCase.invoke().first().forEach { route ->
            publishRouteUseCase.invoke(
                route,
                getRoutePointsListUseCase.invoke(route.routeId).first(),
                id
            )
        }
    }

    private suspend fun fetchRoutes(id: String) {
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