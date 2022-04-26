package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicRouteDomain
import com.example.gh_coursework.domain.entity.PublicRoutePointDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class SavePublicRouteToPrivateUseCaseImpl(private val repository: TravelRepository) : SavePublicRouteToPrivateUseCase {
    override suspend fun invoke(route: PublicRouteDomain, points: List<PublicRoutePointDomain>) {
        repository.savePublicRouteToPrivate(route, points)
    }
}