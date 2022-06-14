package com.example.trampled_trails.domain.usecase.deleted

import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class DeleteAllUseCaseImpl(private val repository: TravelRepository): DeleteAllUseCase {
    override suspend fun invoke() = (Dispatchers.IO) {
        repository.deleteAll()
    }
}