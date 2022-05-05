package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicRouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class SyncRemoteRoutesWithLocalUseCaseImpl(private val repository: TravelRepository) : SyncRemoteRoutesWithLocalUseCase {
    override suspend fun invoke(route: PublicRouteDomain) = (Dispatchers.IO) {
        repository.saveFirebaseRouteToLocal(route)
    }
}