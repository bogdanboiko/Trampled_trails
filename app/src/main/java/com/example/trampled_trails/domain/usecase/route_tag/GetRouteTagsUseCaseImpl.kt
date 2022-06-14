package com.example.trampled_trails.domain.usecase.route_tag

import com.example.trampled_trails.domain.entity.RouteTagDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRouteTagsUseCaseImpl(private val repository: TravelRepository) : GetRouteTagsUseCase {
    override fun invoke(): Flow<List<RouteTagDomain>> {
        return repository.getRouteTags()
    }
}