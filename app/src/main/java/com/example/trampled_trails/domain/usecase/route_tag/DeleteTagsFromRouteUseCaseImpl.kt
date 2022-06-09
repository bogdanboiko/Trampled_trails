package com.example.trampled_trails.domain.usecase.route_tag

import com.example.trampled_trails.domain.entity.RouteTagsDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class DeleteTagsFromRouteUseCaseImpl(private val repository: TravelRepository) :
    DeleteTagsFromRouteUseCase {
    override suspend fun invoke(tagsList: List<RouteTagsDomain>) = (Dispatchers.IO) {
        repository.deleteTagsFromRoute(tagsList)
    }
}