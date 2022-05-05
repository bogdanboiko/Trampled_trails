package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class ClearDeletedPointsTableUseCaseImpl(private val repository: TravelRepository): ClearDeletedPointsTableUseCase {
    override suspend fun invoke() = (Dispatchers.IO) {
        repository.clearDeletedPointsTable()
    }
}