package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicPointDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class SavePublicPointsToPrivateUseCaseImpl(private val repository: TravelRepository): SavePublicPointsToPrivateUseCase {
    override suspend fun invoke(points: List<PublicPointDomain>) {
        repository.saveFirebasePointsToLocal(points)
    }
}