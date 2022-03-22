package com.example.gh_coursework.domain.usecase.route_preview

import com.example.gh_coursework.domain.entity.RouteDomain
import kotlinx.coroutines.flow.Flow

interface GetRoutesListUseCase {
    fun invoke() : Flow<List<RouteDomain>>
}