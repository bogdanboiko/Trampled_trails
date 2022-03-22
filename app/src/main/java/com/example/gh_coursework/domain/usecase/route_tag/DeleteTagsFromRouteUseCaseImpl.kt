package com.example.gh_coursework.domain.usecase.route_tag

import com.example.gh_coursework.domain.entity.RouteTagsDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class DeleteTagsFromRouteUseCaseImpl(private val repository: TravelRepository) :
    DeleteTagsFromRouteUseCase {
    override suspend fun invoke(tagsList: List<RouteTagsDomain>) {
        repository.deleteTagsFromRoute(tagsList)
    }
}