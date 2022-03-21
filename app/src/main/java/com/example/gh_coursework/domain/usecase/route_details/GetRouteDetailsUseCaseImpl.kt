package com.example.gh_coursework.domain.usecase.route_details

import com.example.gh_coursework.domain.entity.RouteDetailsDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRouteDetailsUseCaseImpl(private val repository: TravelRepository): GetRouteDetailsUseCase {
    override fun invoke(routeId: Long): Flow<RouteDetailsDomain> {
        return repository.getRouteDetails(routeId)
    }
}