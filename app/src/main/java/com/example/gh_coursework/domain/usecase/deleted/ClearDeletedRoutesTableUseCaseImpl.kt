package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.repository.TravelRepository

class ClearDeletedRoutesTableUseCaseImpl(private val repository: TravelRepository): ClearDeletedRoutesTableUseCase {
    override suspend fun invoke() {
        repository.clearDeletedRoutesTable()
    }
}