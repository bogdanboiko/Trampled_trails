package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.repository.TravelRepository

class ClearDeletedImagesTableUseCaseImpl(private val repository: TravelRepository) : ClearDeletedImagesTableUseCase {
    override suspend fun invoke() {
        repository.clearDeletedImagesTable()
    }
}