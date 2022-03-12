package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.entity.RouteDomain
import kotlinx.coroutines.flow.Flow

interface GetRoutesListUseCase {
    fun invoke() : Flow<List<RouteDomain>>
}