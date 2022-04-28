package com.example.gh_coursework.domain.usecase.route_preview

import com.example.gh_coursework.domain.repository.TravelRepository

class DeleteAllRoutesUseCaseImpl(private val repository: TravelRepository): DeleteAllRoutesUseCase {
    override suspend fun invoke() {
        repository.deleteAllRoutes()
    }
}