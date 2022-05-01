package com.example.gh_coursework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.deleted.*
import com.example.gh_coursework.domain.usecase.point_preview.GetAllPointsUseCase
import com.example.gh_coursework.domain.usecase.public.*
import com.example.gh_coursework.domain.usecase.route_preview.GetRoutesListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ActivityViewModel(
    private val deleteAllUseCase: DeleteAllUseCase,
    private val deleteRemotePointUseCase: DeleteRemotePointUseCase,
    private val deleteRemoteRouteUseCase: DeleteRemoteRouteUseCase,
    private val clearDeletedPointsTableUseCase: ClearDeletedPointsTableUseCase,
    private val clearDeletesRoutesTableUseCase: ClearDeletedRoutesTableUseCase,
    private val getDeletedRoutesUseCase: GetDeletedRoutesUseCase,
    private val getDeletedPointsUseCase: GetDeletedPointsUseCase,
    private val getRoutesListUseCase: GetRoutesListUseCase,
    private val getPointsListUseCase: GetAllPointsUseCase,
    private val getUserPointsListUseCase: GetUserPointsListUseCase,
    private val getUserRouteListUseCase: GetUserRouteListUseCase,
    private val uploadRouteUseCase: UploadRouteToFirebaseUseCase,
    private val uploadPointsUseCase: UploadPointsToFirebaseUseCase,
    private val savePublicRouteToPrivateUseCase: SavePublicRouteToPrivateUseCase,
    private val savePublicPointsToPrivateUseCase: SavePublicPointsToPrivateUseCase
) : ViewModel() {

    private val deletedRoutes = getDeletedRoutesUseCase.invoke()
    private val deletedPoints = getDeletedPointsUseCase.invoke()

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAllUseCase.invoke()
        }
    }

    fun syncDataWithFirebase(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteRemotePoints()
            deleteRemoteRoutes()
        }.invokeOnCompletion {
            clearDeletedPointsTable()
            clearDeletedRoutesTable()

            viewModelScope.launch(Dispatchers.IO) {
                viewModelScope.launch(Dispatchers.IO) {
                    uploadRoute(userId)
                    uploadPoints(userId)
                }.invokeOnCompletion {
                    viewModelScope.launch(Dispatchers.IO) {
                        fetchRoutes(userId)
                    }.invokeOnCompletion {
                        viewModelScope.launch(Dispatchers.IO) {
                            fetchPoints(userId)
                        }
                    }
                }
            }
        }
    }

    fun uploadDataToFirebase(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteRemotePoints()
            deleteRemoteRoutes()
        }.invokeOnCompletion {
            clearDeletedPointsTable()
            clearDeletedRoutesTable()

            viewModelScope.launch(Dispatchers.IO) {
                viewModelScope.launch(Dispatchers.IO) {
                    uploadRoute(userId)
                    uploadPoints(userId)
                }
            }
        }
    }

    private suspend fun deleteRemotePoints() {
        val deletedPoints = deletedPoints.first()

        if (deletedPoints.isNotEmpty()) {
            deletedPoints.forEach {
                deleteRemotePointUseCase.invoke(it.pointId)
            }
        }
    }

    private suspend fun deleteRemoteRoutes() {
        val deletedRoutes = deletedRoutes.first()

        if (deletedRoutes.isNotEmpty()) {
            deletedRoutes.forEach {
                deleteRemoteRouteUseCase.invoke(it.routeId)
            }
        }
    }

    private fun clearDeletedPointsTable() {
        viewModelScope.launch(Dispatchers.IO) {
            clearDeletedPointsTableUseCase.invoke()
        }
    }

    private fun clearDeletedRoutesTable() {
        viewModelScope.launch(Dispatchers.IO) {
            clearDeletesRoutesTableUseCase.invoke()
        }
    }

    private suspend fun uploadRoute(id: String) {
        getRoutesListUseCase.invoke().first().forEach { route ->
            uploadRouteUseCase.invoke(
                route,
                id
            )
        }
    }

    private suspend fun uploadPoints(userId: String) {
        uploadPointsUseCase.invoke(getPointsListUseCase.invoke().first(), userId)
    }

    private suspend fun fetchRoutes(id: String) {
        getUserRouteListUseCase.invoke(id).collect { routesToSave ->
            routesToSave.forEach { route ->
                savePublicRouteToPrivateUseCase.invoke(route)
            }
        }
    }

    private suspend fun fetchPoints(userId: String) {
        getUserPointsListUseCase.invoke(userId).collect { pointsToSave ->
            savePublicPointsToPrivateUseCase.invoke(pointsToSave)
        }
    }
}