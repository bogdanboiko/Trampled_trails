package com.example.gh_coursework.domain.usecase.route_details

import com.example.gh_coursework.domain.entity.RouteDetailsDomain

interface UpdateRouteDetailsUseCase {
    suspend fun invoke(route: RouteDetailsDomain)
}