package com.example.trampled_trails.domain.usecase.route_tag

import com.example.trampled_trails.domain.entity.RouteTagsDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddRouteTagsUseCaseImpl(private val repository: TravelRepository): AddRouteTagsUseCase {
    override suspend fun invoke(tagsList: List<RouteTagsDomain>) = (Dispatchers.IO) {
        repository.addRouteTagsList(tagsList)
    }
}