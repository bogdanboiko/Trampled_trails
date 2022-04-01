package com.example.gh_coursework.domain.usecase.route_details

import com.example.gh_coursework.domain.entity.RouteDomain

interface UpdateRouteDetailsUseCase {
    suspend fun invoke(route: RouteDomain)
}