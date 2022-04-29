package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.repository.TravelRepository

class ClearDeletedPointsTableUseCaseImpl(private val repository: TravelRepository): ClearDeletedPointsTableUseCase {
    override suspend fun invoke() {
        repository.clearDeletedPointsTable()
    }
}