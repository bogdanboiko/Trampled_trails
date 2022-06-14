package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class ChangeRouteAccessUseCaseImpl(private val repository: TravelRepository): ChangeRouteAccessUseCase {
    override suspend fun invoke(routeId: String, isPublic: Boolean) = (Dispatchers.IO) {
        repository.changeRouteAccess(routeId, isPublic)
    }
}