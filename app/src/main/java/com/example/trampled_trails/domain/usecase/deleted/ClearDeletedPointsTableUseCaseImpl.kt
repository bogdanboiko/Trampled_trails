package com.example.trampled_trails.domain.usecase.deleted

import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class ClearDeletedPointsTableUseCaseImpl(private val repository: TravelRepository): ClearDeletedPointsTableUseCase {
    override suspend fun invoke() = (Dispatchers.IO) {
        repository.clearDeletedPointsTable()
    }
}