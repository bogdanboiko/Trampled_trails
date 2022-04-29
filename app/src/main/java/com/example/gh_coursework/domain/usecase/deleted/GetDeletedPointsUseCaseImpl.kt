package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.entity.DeletedPointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetDeletedPointsUseCaseImpl(private val repository: TravelRepository): GetDeletedPointsUseCase {
    override fun invoke(): Flow<List<DeletedPointDomain>> {
        return repository.getDeletedPoints()
    }
}