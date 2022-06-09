package com.example.trampled_trails.domain.usecase.route_preview

import com.example.trampled_trails.domain.entity.RouteDomain
import com.example.trampled_trails.domain.entity.PointPreviewDomain

interface AddRouteUseCase {
    suspend fun invoke(route: RouteDomain, pointsList: List<PointPreviewDomain>)
}