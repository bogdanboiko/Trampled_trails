package com.example.gh_coursework.domain.usecase.route

import com.example.gh_coursework.domain.entity.RouteDomain

interface DeleteRouteUseCase {
    suspend fun invoke(routeId: Int)
}