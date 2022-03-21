package com.example.gh_coursework.domain.usecase.route_tag

import com.example.gh_coursework.domain.entity.RouteTagsDomain

interface AddRouteTagsUseCase {
    suspend fun invoke(tagsList: List<RouteTagsDomain>)
}