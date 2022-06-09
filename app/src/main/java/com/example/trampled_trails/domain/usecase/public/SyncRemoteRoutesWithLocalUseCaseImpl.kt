package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.PublicRouteDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class SyncRemoteRoutesWithLocalUseCaseImpl(private val repository: TravelRepository) : SyncRemoteRoutesWithLocalUseCase {
    override suspend fun invoke(route: PublicRouteDomain) = (Dispatchers.IO) {
        repository.saveFirebaseRouteToLocal(route)
    }
}