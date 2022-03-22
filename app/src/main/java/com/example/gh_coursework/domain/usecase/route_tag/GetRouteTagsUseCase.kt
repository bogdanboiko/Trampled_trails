package com.example.gh_coursework.domain.usecase.route_tag

import com.example.gh_coursework.domain.entity.RouteTagDomain
import kotlinx.coroutines.flow.Flow

interface GetRouteTagsUseCase {
    fun invoke(): Flow<List<RouteTagDomain>>
}