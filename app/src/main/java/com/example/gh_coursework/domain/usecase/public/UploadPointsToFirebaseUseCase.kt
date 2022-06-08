package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PointDomain

interface UploadPointsToFirebaseUseCase {
    suspend fun invoke(points: List<PointDomain>, currentUser: String)
}