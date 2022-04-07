package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointDomain

interface PublishRouteUseCase {
    fun invoke(route: RouteDomain, routePoints: List<RoutePointDomain>)
}