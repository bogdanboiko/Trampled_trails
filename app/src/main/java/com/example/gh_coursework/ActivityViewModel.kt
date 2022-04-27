package com.example.gh_coursework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.public.FetchRoutePointsFromRemoteUseCase
import com.example.gh_coursework.domain.usecase.public.GetUserRouteListUseCase
import com.example.gh_coursework.domain.usecase.public.PublishRouteUseCase
import com.example.gh_coursework.domain.usecase.public.SavePublicRouteToPrivateUseCase
import com.example.gh_coursework.domain.usecase.route_points.GetRoutePointsListUseCase
import com.example.gh_coursework.domain.usecase.route_preview.GetRoutesListUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ActivityViewModel(
    private val getRoutesListUseCase: GetRoutesListUseCase,
    private val getRoutePointsListUseCase: GetRoutePointsListUseCase,
    private val publishRouteUseCase: PublishRouteUseCase,
    private val getUserRouteListUseCase: GetUserRouteListUseCase,
    private val fetchRoutePointsFromRemoteUseCase: FetchRoutePointsFromRemoteUseCase,
    private val savePublicRouteToPrivateUseCase: SavePublicRouteToPrivateUseCase
) : ViewModel() {
    fun uploadActualRoutesToFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
            var user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
//                Tasks.await(FirebaseAuth.getInstance().signInAnonymously().addOnSuccessListener {
//                    user = it.user
//                })
                return@launch
            }
            viewModelScope.launch(Dispatchers.IO) {
                getRoutesListUseCase.invoke().first().forEach { route ->
                    publishRouteUseCase.invoke(
                        route,
                        getRoutePointsListUseCase.invoke(route.routeId).first(),
                        user.uid
                    )
                }
            }.invokeOnCompletion {
                viewModelScope.launch(Dispatchers.IO) {
                    getUserRouteListUseCase.invoke(user.uid).collect { routesToSave ->
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