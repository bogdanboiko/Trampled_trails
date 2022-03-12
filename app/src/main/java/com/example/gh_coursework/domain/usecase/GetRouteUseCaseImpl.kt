package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRouteUseCaseImpl(private val repository: TravelRepository) : GetRouteUseCase {
    override fun invoke(): Flow<RouteDomain> {
        return repository.getRoute()
    }
}