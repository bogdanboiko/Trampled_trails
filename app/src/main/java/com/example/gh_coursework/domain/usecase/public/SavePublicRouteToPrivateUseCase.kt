package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicRouteDomain

interface SavePublicRouteToPrivateUseCase {
    suspend fun invoke(route: PublicRouteDomain, currentUser: String)
}