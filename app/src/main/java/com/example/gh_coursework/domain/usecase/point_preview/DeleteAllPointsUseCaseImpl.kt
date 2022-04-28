package com.example.gh_coursework.domain.usecase.point_preview

import com.example.gh_coursework.domain.repository.TravelRepository

class DeleteAllPointsUseCaseImpl(private val repository: TravelRepository): DeleteAllPointsUseCase {
    override suspend fun invoke() {
        repository.deleteAllPoints()
    }
}