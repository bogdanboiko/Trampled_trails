package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicRouteDomain

interface SyncRemoteRoutesWithLocalUseCase {
    suspend fun invoke(route: PublicRouteDomain)
}