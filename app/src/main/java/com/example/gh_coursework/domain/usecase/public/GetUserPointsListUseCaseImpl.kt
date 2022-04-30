package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicPointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetUserPointsListUseCaseImpl(private val repository: TravelRepository): GetUserPointsListUseCase {
    override fun invoke(userId: String): Flow<List<PublicPointDomain>> {
        return repository.getUserPoints(userId)
    }
}