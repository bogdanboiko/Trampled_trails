package com.example.gh_coursework.domain.usecase.route_preview

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointDomain

interface AddRouteUseCase {
    suspend fun invoke(route: RouteDomain, pointsList: List<RoutePointDomain>)
}