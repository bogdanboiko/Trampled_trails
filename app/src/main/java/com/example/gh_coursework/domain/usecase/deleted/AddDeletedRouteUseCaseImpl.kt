package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.entity.DeletedRouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class AddDeletedRouteUseCaseImpl(private val repository: TravelRepository): AddDeletedRouteUseCase {
    override suspend fun invoke(route: DeletedRouteDomain) {
        repository.addDeletedRoute(route)
    }
}