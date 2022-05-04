package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.entity.DeletedPointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddDeletedPointUseCaseImpl(private val repository: TravelRepository): AddDeletedPointUseCase {
    override suspend fun invoke(point: DeletedPointDomain) = (Dispatchers.IO) {
        repository.addDeletedPoint(point)
    }
}