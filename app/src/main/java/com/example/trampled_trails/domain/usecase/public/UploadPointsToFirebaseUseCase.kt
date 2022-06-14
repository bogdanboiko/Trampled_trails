package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.PointDomain

interface UploadPointsToFirebaseUseCase {
    suspend fun invoke(points: List<PointDomain>, currentUser: String)
}