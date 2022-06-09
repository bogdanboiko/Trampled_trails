package com.example.trampled_trails.domain.usecase.route_preview

import com.example.trampled_trails.domain.entity.RouteDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRoutesListUseCaseImpl(private val repository: TravelRepository): GetRoutesListUseCase {
    override fun invoke(): Flow<List<RouteDomain>> {
        return repository.getRoutesList()
    }
}