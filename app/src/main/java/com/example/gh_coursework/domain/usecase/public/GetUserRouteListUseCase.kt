package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicRouteDomain
import kotlinx.coroutines.flow.Flow

interface GetUserRouteListUseCase {
    fun invoke(userId: String): Flow<List<PublicRouteDomain>>
}