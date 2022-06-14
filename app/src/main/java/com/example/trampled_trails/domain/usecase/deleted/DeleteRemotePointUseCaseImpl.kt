package com.example.trampled_trails.domain.usecase.deleted

import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class DeleteRemotePointUseCaseImpl(private val repository: TravelRepository): DeleteRemotePointUseCase {
    override suspend fun invoke(pointId: String) = (Dispatchers.IO) {
        repository.deleteRemotePoint(pointId)
    }
}