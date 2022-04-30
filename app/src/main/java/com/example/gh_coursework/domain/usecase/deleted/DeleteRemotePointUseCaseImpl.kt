package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.repository.TravelRepository

class DeleteRemotePointUseCaseImpl(private val repository: TravelRepository): DeleteRemotePointUseCase {
    override suspend fun invoke(pointId: String) {
        repository.deleteRemotePoint(pointId)
    }
}