package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicPointDomain

interface SyncRemotePointsWithLocalUseCase {
    suspend fun invoke(points: List<PublicPointDomain>)
}