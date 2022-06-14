package com.example.trampled_trails.domain.usecase.route_preview

import com.example.trampled_trails.domain.entity.RouteDomain
import kotlinx.coroutines.flow.Flow

interface GetRoutesListUseCase {
    fun invoke() : Flow<List<RouteDomain>>
}