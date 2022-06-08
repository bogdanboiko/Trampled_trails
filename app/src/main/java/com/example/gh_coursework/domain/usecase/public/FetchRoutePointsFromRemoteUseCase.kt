package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicPointDomain
import kotlinx.coroutines.flow.Flow

interface FetchRoutePointsFromRemoteUseCase {
    fun invoke(routeId: String): Flow<List<PublicPointDomain>>
}