package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicRouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetUserRouteListUseCaseImpl(private val repository: TravelRepository) : GetUserRouteListUseCase {
    override fun invoke(userId: String): Flow<List<PublicRouteDomain>> {
        return repository.getUserRoutes(userId)
    }
}