package com.example.gh_coursework.domain.usecase.route_tag

import com.example.gh_coursework.domain.entity.RouteTagsDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddRouteTagsUseCaseImpl(private val repository: TravelRepository): AddRouteTagsUseCase {
    override suspend fun invoke(tagsList: List<RouteTagsDomain>) = (Dispatchers.IO) {
        repository.addRouteTagsList(tagsList)
    }
}