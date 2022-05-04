package com.example.gh_coursework.domain.usecase.route_preview

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.PointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddRouteUseCaseImpl(private val repository: TravelRepository): AddRouteUseCase {
    override suspend fun invoke(route: RouteDomain, pointsList: List<PointDomain>) = (Dispatchers.IO) {
        repository.addRoute(route, pointsList)
    }
}