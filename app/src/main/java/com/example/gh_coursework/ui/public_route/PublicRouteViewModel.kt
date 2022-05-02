package com.example.gh_coursework.ui.public_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.gh_coursework.data.remote.FirestorePagingSource
import com.example.gh_coursework.domain.usecase.public.*
import com.example.gh_coursework.domain.usecase.route_details.GetPublicRouteListUseCase
import com.example.gh_coursework.ui.public_route.mapper.mapPublicRouteDomainToModel
import com.example.gh_coursework.ui.public_route.mapper.mapRoutePointDomainToModel
import com.example.gh_coursework.ui.public_route.model.PublicRouteModel
import com.example.gh_coursework.ui.public_route.model.RoutePointModel
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class PublicRouteViewModel(
    private val query: Query,
    private val getAllFavouritesUseCase: GetAllFavouritesUseCase,
    private val addRouteToFavouritesUseCase: AddRouteToFavouritesUseCase,
    private val removeRouteFromFavouritesUseCase: RemoveRouteFromFavouritesUseCase,
    private val fetchRoutePointsUseCase: FetchRoutePointsFromRemoteUseCase,
    private val getPublicRouteListUseCase: GetPublicRouteListUseCase,
    private val makePublicRoutePrivateUseCase: MakePublicRoutePrivateUseCase
) : ViewModel() {

    val favourites = getAllFavouritesUseCase.invoke()

    fun fetchRoutes(tagsFilter: List<String>) = Pager(
    PagingConfig(pageSize = 10)
    ) {
        FirestorePagingSource(query, tagsFilter)
    }.flow.cachedIn(viewModelScope)

    fun addRouteToFavourites(routeId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addRouteToFavouritesUseCase.invoke(routeId, userId)
        }
    }

    fun removeRouteFromFavourites(routeId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            removeRouteFromFavouritesUseCase.invoke(routeId, userId)
        }
    }

    fun fetchRoutePoints(routeId: String): Flow<List<RoutePointModel>> {
        return fetchRoutePointsUseCase.invoke(routeId).map { it.map(::mapRoutePointDomainToModel) }
    }

    fun fetchPublicRouteList(): Flow<List<PublicRouteModel>>  {
        return getPublicRouteListUseCase.invoke().map { it.map(::mapPublicRouteDomainToModel) }
    }

    fun archiveRoute(routeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            makePublicRoutePrivateUseCase.invoke(routeId)
        }
    }
}