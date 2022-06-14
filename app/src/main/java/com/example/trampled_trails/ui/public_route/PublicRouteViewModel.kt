package com.example.trampled_trails.ui.public_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.trampled_trails.data.remote.paging_source.PublicFavouritesPagingSource
import com.example.trampled_trails.data.remote.paging_source.PublicTagsPagingSource
import com.example.trampled_trails.domain.usecase.public.AddRouteToFavouritesUseCase
import com.example.trampled_trails.domain.usecase.public.FetchRoutePointsFromRemoteUseCase
import com.example.trampled_trails.domain.usecase.public.GetUserFavouriteRoutesUseCase
import com.example.trampled_trails.domain.usecase.public.RemoveRouteFromFavouritesUseCase
import com.example.trampled_trails.ui.public_route.mapper.mapRoutePointDomainToModel
import com.example.trampled_trails.ui.public_route.model.RoutePointModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PublicRouteViewModel(
    private val getUserFavouriteRoutesUseCase: GetUserFavouriteRoutesUseCase,
    private val addRouteToFavouritesUseCase: AddRouteToFavouritesUseCase,
    private val removeRouteFromFavouritesUseCase: RemoveRouteFromFavouritesUseCase,
    private val fetchRoutePointsUseCase: FetchRoutePointsFromRemoteUseCase
) : ViewModel() {

    fun fetchTaggedRoutes(tagsFilter: List<String>) = Pager(PagingConfig(pageSize = 10)) {
        PublicTagsPagingSource(
            FirebaseFirestore.getInstance()
                .collection("routes").whereEqualTo("public", true)
                .limit(10),
            tagsFilter
        )
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