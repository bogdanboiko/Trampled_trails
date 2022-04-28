package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.repository.TravelRepository

class DeleteAllUseCaseImpl(private val repository: TravelRepository): DeleteAllUseCase {
    override suspend fun invoke() {
        repository.deleteAllRoutes()
        repository.deleteAllPoints()
    }
}