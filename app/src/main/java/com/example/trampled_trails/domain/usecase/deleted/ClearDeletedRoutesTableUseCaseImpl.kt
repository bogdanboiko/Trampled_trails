package com.example.trampled_trails.domain.usecase.deleted

import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class ClearDeletedRoutesTableUseCaseImpl(private val repository: TravelRepository): ClearDeletedRoutesTableUseCase {
    override suspend fun invoke() = (Dispatchers.IO) {
        repository.clearDeletedRoutesTable()
    }
}