package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.PublicPointDomain
import kotlinx.coroutines.flow.Flow

interface FetchRoutePointsFromRemoteUseCase {
    fun invoke(routeId: String): Flow<List<PublicPointDomain>>
}