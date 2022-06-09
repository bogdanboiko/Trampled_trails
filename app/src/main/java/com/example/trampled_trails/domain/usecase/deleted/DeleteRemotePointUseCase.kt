package com.example.trampled_trails.domain.usecase.deleted

interface DeleteRemotePointUseCase {
    suspend fun invoke(pointId: String)
}