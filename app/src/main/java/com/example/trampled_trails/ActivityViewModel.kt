package com.example.trampled_trails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trampled_trails.domain.usecase.deleted.*
import com.example.trampled_trails.domain.usecase.main_activity.GetAllPointsUseCase
import com.example.trampled_trails.domain.usecase.public.*
import com.example.trampled_trails.domain.usecase.route_preview.GetRoutesListUseCase
import com.example.trampled_trails.ui.homepage.data.SyncingProgressState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ActivityViewModel(
    private val deleteAllUseCase: DeleteAllUseCase,
    private val deleteRemotePointUseCase: DeleteRemotePointUseCase,
    private val deleteRemoteRouteUseCase: DeleteRemoteRouteUseCase,
    private val deleteImagesFromFirestoreUseCase: DeleteImagesFromFirestoreUseCase,
    private val getDeletedImagesUseCase: GetDeletedImagesUseCase,
    private val clearDeletedImagesTableUseCase: ClearDeletedImagesTableUseCase,
    private val clearDeletedPointsTableUseCase: ClearDeletedPointsTableUseCase,
    private val clearDeletesRoutesTableUseCase: ClearDeletedRoutesTableUseCase,
    private val getDeletedRoutesUseCase: GetDeletedRoutesUseCase,
    private val getDeletedPointsUseCase: GetDeletedPointsUseCase,
    private val getRoutesListUseCase: GetRoutesListUseCase,
    private val getPointsListUseCase: GetAllPointsUseCase,
    private val getUserPointsListUseCase: GetUserPointListUseCase,
    private val getUserRouteListUseCase: GetUserRouteListUseCase,
    private val uploadRouteUseCase: UploadRouteToFirebaseUseCase,
    private val uploadPointsUseCase: UploadPointsToFirebaseUseCase,
    private val savePublicRouteToPrivateUseCase: SyncRemoteRoutesWithLocalUseCase,
    private val savePublicPointsToPrivateUseCase: SyncRemotePointsWithLocalUseCase
) : ViewModel() {

    private val deletedRoutes = getDeletedRoutesUseCase.invoke()
    private val deletedPoints = getDeletedPointsUseCase.invoke()
    private val _syncProgress = MutableSharedFlow<SyncingProgressState>(1)
    val syncProgress: SharedFlow<SyncingProgressState> = _syncProgress

    fun deleteAll() {
        viewModelScope.launch {
            deleteAllUseCase.invoke()
        }
    }

    fun syncDataWithFirebase(userId: String) {
        viewModelScope.launch {
            _syncProgress.emit(SyncingProgressState.Loading)
            deleteRemotePoints()
            deleteRemoteRoutes()
            deleteImages()
            clearDeletedPointsTable()
            clearDeletedRoutesTable()
            clearDeletedImagesTable()
            uploadRoutes(userId)
            uploadPoints(userId)
            fetchRoutes(userId)
            fetchPoints(userId)
            _syncProgress.emit(SyncingProgressState.FinishedSync)
        }
    }

    private suspend fun clearDeletedImagesTable() {
        clearDeletedImagesTableUseCase.invoke()
    }

    private suspend fun deleteImages() {
        deleteImagesFromFirestoreUseCase(getDeletedImagesUseCase.invoke().first())
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
        viewModelScope.launch {
            clearDeletedPointsTableUseCase.invoke()
        }
    }

    private fun clearDeletedRoutesTable() {
        viewModelScope.launch {
            clearDeletesRoutesTableUseCase.invoke()
        }
    }

    private suspend fun uploadPoints(userId: String) {
        uploadPointsUseCase.invoke(getPointsListUseCase.invoke().first(), userId)
    }

    private suspend fun uploadRoutes(userId: String) {
        getRoutesListUseCase.invoke().first().forEach { route ->
            uploadRouteUseCase.invoke(
                route,
                userId
            )
        }
    }

    private suspend fun fetchPoints(userId: String) {
        getUserPointsListUseCase.invoke(userId).collect { pointsToSave ->
            savePublicPointsToPrivateUseCase.invoke(pointsToSave)
        }
    }

    private suspend fun fetchRoutes(userId: String) {
        getUserRouteListUseCase.invoke(userId).collect { routesToSave ->
            routesToSave.forEach { route ->
                savePublicRouteToPrivateUseCase.invoke(route)
            }
        }
    }
}