package com.example.gh_coursework.domain.usecase.route_preview

import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRoutesListUseCaseImpl(private val repository: TravelRepository): GetRoutesListUseCase {
    override fun invoke(): Flow<List<RouteDomain>> {
        return repository.getRoutesList()
    }
}