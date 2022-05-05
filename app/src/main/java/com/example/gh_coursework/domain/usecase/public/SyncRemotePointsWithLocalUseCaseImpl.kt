package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicPointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class SyncRemotePointsWithLocalUseCaseImpl(private val repository: TravelRepository): SyncRemotePointsWithLocalUseCase {
    override suspend fun invoke(points: List<PublicPointDomain>) = (Dispatchers.IO) {
        repository.saveFirebasePointsToLocal(points)
    }
}