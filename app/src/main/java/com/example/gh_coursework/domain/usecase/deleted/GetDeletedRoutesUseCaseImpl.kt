package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.entity.DeletedRouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetDeletedRoutesUseCaseImpl(private val repository: TravelRepository): GetDeletedRoutesUseCase {
    override fun invoke(): Flow<List<DeletedRouteDomain>> {
        return repository.getDeletedRoutes()
    }
}