package com.example.trampled_trails.domain.usecase.deleted

import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetDeletedImagesUseCaseImpl(private val repository: TravelRepository) : GetDeletedImagesUseCase {
    override fun invoke(): Flow<List<String>> {
        return repository.getImageListToDelete()
    }
}