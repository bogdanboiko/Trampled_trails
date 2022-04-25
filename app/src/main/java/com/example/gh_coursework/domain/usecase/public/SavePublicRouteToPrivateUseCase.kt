package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicRouteDomain
import com.example.gh_coursework.domain.entity.PublicRoutePointDomain

interface SavePublicRouteToPrivateUseCase {
    suspend fun invoke(route: PublicRouteDomain, points: List<PublicRoutePointDomain>)
}