package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.PublicRouteDomain
import kotlinx.coroutines.flow.Flow

interface GetUserRouteListUseCase {
    fun invoke(userId: String): Flow<List<PublicRouteDomain>>
}