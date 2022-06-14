package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.PublicPointDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class SyncRemotePointsWithLocalUseCaseImpl(private val repository: TravelRepository): SyncRemotePointsWithLocalUseCase {
    override suspend fun invoke(points: List<PublicPointDomain>) = (Dispatchers.IO) {
        repository.saveFirebasePointsToLocal(points)
    }
}