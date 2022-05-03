package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetDeletedImagesUseCaseImpl(private val repository: TravelRepository) : GetDeletedImagesUseCase {
    override fun invoke(): Flow<List<String>> {
        return repository.getImageListToDelete()
    }
}