package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.PublicRouteDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetUserRouteListUseCaseImpl(private val repository: TravelRepository) : GetUserRouteListUseCase {
    override fun invoke(userId: String): Flow<List<PublicRouteDomain>> {
        return repository.getUserRoutes(userId)
    }
}