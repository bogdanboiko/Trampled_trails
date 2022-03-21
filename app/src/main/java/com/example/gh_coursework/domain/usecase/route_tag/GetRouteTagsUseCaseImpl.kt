package com.example.gh_coursework.domain.usecase.route_tag

import com.example.gh_coursework.domain.entity.RouteTagDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRouteTagsUseCaseImpl(private val repository: TravelRepository) : GetRouteTagsUseCase {
    override fun invoke(): Flow<List<RouteTagDomain>> {
        return repository.getRouteTags()
    }
}