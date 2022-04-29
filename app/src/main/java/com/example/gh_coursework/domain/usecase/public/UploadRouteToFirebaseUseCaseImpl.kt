package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.PointDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class UploadRouteToFirebaseUseCaseImpl(private val repository: TravelRepository): UploadRouteToFirebaseUseCase {
    override suspend fun invoke(
        route: RouteDomain,
        routePoints: List<PointDomain>,
        currentUser: String
    ) {
        repository.uploadRouteToFirebase(route, routePoints, currentUser)
    }
}