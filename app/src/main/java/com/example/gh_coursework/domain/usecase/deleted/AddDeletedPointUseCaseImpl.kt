package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.entity.DeletedPointDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class AddDeletedPointUseCaseImpl(private val repository: TravelRepository): AddDeletedPointUseCase {
    override suspend fun invoke(point: DeletedPointDomain) {
        repository.addDeletedPoint(point)
    }
}