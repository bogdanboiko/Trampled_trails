package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class DeleteAllUseCaseImpl(private val repository: TravelRepository): DeleteAllUseCase {
    override suspend fun invoke() = (Dispatchers.IO) {
        repository.deleteAll()
    }
}