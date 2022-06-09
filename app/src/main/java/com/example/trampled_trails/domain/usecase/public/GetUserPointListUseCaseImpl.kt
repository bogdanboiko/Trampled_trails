package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.PublicPointDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetUserPointListUseCaseImpl(private val repository: TravelRepository): GetUserPointListUseCase {
    override fun invoke(userId: String): Flow<List<PublicPointDomain>> {
        return repository.getUserPoints(userId)
    }
}