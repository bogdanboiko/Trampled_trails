package com.example.gh_coursework.ui.public_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.gh_coursework.data.remote.paging_source.PublicFavouritesPagingSource
import com.example.gh_coursework.data.remote.paging_source.PublicTagsPagingSource
import com.example.gh_coursework.domain.usecase.public.*
import com.example.gh_coursework.ui.public_route.mapper.mapRoutePointDomainToModel
import com.example.gh_coursework.ui.public_route.model.RoutePointModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PublicRouteViewModel(
    private val query: Query,
    private val getUserFavouriteRoutesUseCase: GetUserFavouriteRoutesUseCase,
    private val addRouteToFavouritesUseCase: AddRouteToFavouritesUseCase,
    private val removeRouteFromFavouritesUseCase: RemoveRouteFromFavouritesUseCase,
    private val fetchRoutePointsUseCase: FetchRoutePointsFromRemoteUseCase
) : ViewModel() {

    fun fetchTaggedRoutes(tagsFilter: List<String>) = Pager(PagingConfig(pageSize = 10)) {
        PublicTagsPagingSource(query, tagsFilter)
    }.flow.cachedIn(viewModelScope)

    fun fetchFavouriteRoutes(routesIdList: List<String>) = Pager(PagingConfig(pageSize = 10)) {
        PublicFavouritesPagingSource(
            FirebaseFirestore.getInstance().collection("routes"),
            routesIdList
        )
    }.flow.cachedIn(viewModelScope)

    fun getFavouriteRoutes(userId: String): Flow<List<String>> {
        return getUserFavouriteRoutesUseCase.invoke(userId)
    }

    fun addRouteToFavourites(routeId: String, userId: String) {
        viewModelScope.launch {
            addRouteToFavouritesUseCase.invoke(routeId, userId)
        }
    }

    fun removeRouteFromFavourites(routeId: String, userId: String) {
        viewModelScope.launch {
            removeRouteFromFavouritesUseCase.invoke(routeId, userId)
        }
    }

    fun fetchRoutePoints(routeId: String): Flow<List<RoutePointModel>> {
        return fetchRoutePointsUseCase.invoke(routeId).map { it.map(::mapRoutePointDomainToModel) }
    }
}