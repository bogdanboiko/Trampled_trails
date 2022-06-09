package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.PublicRouteDomain

interface SyncRemoteRoutesWithLocalUseCase {
    suspend fun invoke(route: PublicRouteDomain)
}