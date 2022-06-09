package com.example.trampled_trails.domain.usecase.deleted

import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class DeleteRemoteRouteUseCaseImpl(private val repository: TravelRepository): DeleteRemoteRouteUseCase {
    override suspend fun invoke(routeId: String) = (Dispatchers.IO) {
        repository.deleteRemoteRoute(routeId)
    }
}