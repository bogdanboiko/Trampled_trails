package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.RouteDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class UploadRouteToFirebaseUseCaseImpl(private val repository: TravelRepository): UploadRouteToFirebaseUseCase {
    override suspend fun invoke(
        route: RouteDomain,
        currentUser: String
    ) = (Dispatchers.IO) {
        repository.uploadRouteToFirebase(route, currentUser)
    }
}