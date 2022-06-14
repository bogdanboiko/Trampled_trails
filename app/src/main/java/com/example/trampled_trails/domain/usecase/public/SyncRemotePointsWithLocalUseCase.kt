package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.PublicPointDomain

interface SyncRemotePointsWithLocalUseCase {
    suspend fun invoke(points: List<PublicPointDomain>)
}