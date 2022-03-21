package com.example.gh_coursework.domain.usecase.route_preview

import com.example.gh_coursework.domain.entity.RouteDomain

interface DeleteRouteUseCase {
    suspend fun invoke(route: RouteDomain)
}