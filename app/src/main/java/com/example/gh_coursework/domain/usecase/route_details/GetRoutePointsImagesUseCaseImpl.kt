package com.example.gh_coursework.domain.usecase.route_details

import com.example.gh_coursework.domain.entity.RoutePointsImagesDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRoutePointsImagesUseCaseImpl(private val repository: TravelRepository) :
    GetRoutePointsImagesUseCase {
    override fun invoke(routeId: Long): Flow<List<RoutePointsImagesDomain>> {
        return repository.getRoutePointsImagesList(routeId)
    }
}