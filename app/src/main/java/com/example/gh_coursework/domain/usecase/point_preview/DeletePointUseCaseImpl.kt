package com.example.gh_coursework.domain.usecase.point_preview

import com.example.gh_coursework.domain.repository.TravelRepository

class DeletePointUseCaseImpl(private val repository: TravelRepository) : DeletePointUseCase {
    override suspend fun invoke(pointId: Int) {
        repository.deletePoint(pointId)
    }
}