package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.google.firebase.auth.FirebaseUser

interface PublishRouteUseCase {
    suspend fun invoke(route: RouteDomain, routePoints: List<RoutePointDomain>, currentUser: String)
}