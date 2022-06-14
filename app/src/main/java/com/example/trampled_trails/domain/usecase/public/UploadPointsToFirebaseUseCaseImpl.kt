package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.PointDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class UploadPointsToFirebaseUseCaseImpl(private val repository: TravelRepository): UploadPointsToFirebaseUseCase {
    override suspend fun invoke(points: List<PointDomain>, currentUser: String) = (Dispatchers.IO) {
        repository.uploadPointsToFirebase(points, currentUser)
    }
}