package com.example.trampled_trails.domain.usecase.deleted

interface ClearDeletedPointsTableUseCase {
    suspend fun invoke()
}