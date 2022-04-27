package com.example.gh_coursework.ui.public_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.gh_coursework.data.remote.FirestorePagingSource
import com.example.gh_coursework.domain.entity.PublicRouteDomain
import com.example.gh_coursework.domain.entity.PublicRoutePointDomain
import com.example.gh_coursework.domain.usecase.public.FetchRoutePointsFromRemoteUseCase
import com.example.gh_coursework.domain.usecase.public.SavePublicRouteToPrivateUseCase
import com.example.gh_coursework.domain.usecase.route_details.GetPublicRouteListUseCase
import com.example.gh_coursework.ui.private_route.mapper.mapRouteDomainToModel
import com.example.gh_coursework.ui.private_route.model.RouteModel
import com.example.gh_coursework.ui.public_route.mapper.mapRoutePointDomainToModel
import com.example.gh_coursework.ui.public_route.model.RoutePointModel
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PublicRouteViewModel(
    private val query: Query,
    private val fetchRoutePointsUseCase: FetchRoutePointsFromRemoteUseCase,
    private val savePublicRouteToPrivateUseCase: SavePublicRouteToPrivateUseCase,
    private val getPublicRouteListUseCase: GetPublicRouteListUseCase
) : ViewModel() {
    fun fetchRoutes(tagsFilter: List<String>) = Pager(
    PagingConfig(pageSize = 10)
    ) {
        FirestorePagingSource(query, tagsFilter)
    }.flow.cachedIn(viewModelScope)

    fun fetchRoutePoints(routeId: String): Flow<List<RoutePointModel>> {
        return fetchRoutePointsUseCase.invoke(routeId).map { it.map(::mapRoutePointDomainToModel) }
    }

    suspend fun savePublicRouteToPrivate(route: PublicRouteDomain, points: List<PublicRoutePointDomain>) {
        savePublicRouteToPrivateUseCase.invoke(route, points)
    }

    fun fetchPublicRouteList(): Flow<List<RouteModel>>  {
        return getPublicRouteListUseCase.invoke().map { it.map(::mapRouteDomainToModel) }
    }
}