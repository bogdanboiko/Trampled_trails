package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository
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