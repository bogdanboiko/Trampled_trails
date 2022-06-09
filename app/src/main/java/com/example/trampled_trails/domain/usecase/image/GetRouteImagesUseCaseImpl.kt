package com.example.trampled_trails.domain.usecase.image

import com.example.trampled_trails.domain.entity.RouteImageDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRouteImagesUseCaseImpl(private val repository: TravelRepository): GetRouteImagesUseCase {
    override fun invoke(routeId: String): Flow<List<RouteImageDomain>> {
        return repository.getRouteImages(routeId)
    }
}