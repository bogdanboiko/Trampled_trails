package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.PointDomain

interface UploadRouteToFirebaseUseCase {
    suspend fun invoke(route: RouteDomain, routePoints: List<PointDomain>, currentUser: String)
}