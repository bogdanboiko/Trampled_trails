package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class DeleteRemotePointUseCaseImpl(private val repository: TravelRepository): DeleteRemotePointUseCase {
    override suspend fun invoke(pointId: String) = (Dispatchers.IO) {
        repository.deleteRemotePoint(pointId)
    }
}