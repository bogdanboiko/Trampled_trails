package com.example.gh_coursework.domain.usecase.route_preview

import com.example.gh_coursework.domain.entity.DeletedRouteDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class DeleteRouteUseCaseImpl(private val repository: TravelRepository) : DeleteRouteUseCase {
    override suspend fun invoke(route: RouteDomain) = (Dispatchers.IO) {
        repository.deleteRoute(route)
        repository.addDeletedRoute(DeletedRouteDomain(route.routeId))
    }
}