package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PointDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class UploadPointsToFirebaseUseCaseImpl(private val repository: TravelRepository): UploadPointsToFirebaseUseCase {
    override suspend fun invoke(points: List<PointDomain>, currentUser: String) {
        repository.uploadPointsToFirebase(points, currentUser)
    }
}