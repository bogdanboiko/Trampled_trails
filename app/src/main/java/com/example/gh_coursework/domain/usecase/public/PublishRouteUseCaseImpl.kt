package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import com.google.firebase.auth.FirebaseUser

class PublishRouteUseCaseImpl(private val repository: TravelRepository): PublishRouteUseCase {
    override fun invoke(
        route: RouteDomain,
        routePoints: List<RoutePointDomain>,
        currentUser: FirebaseUser
    ) {
        repository.publishRoute(route, routePoints, currentUser)
    }
}