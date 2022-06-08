package com.example.gh_coursework.domain.usecase.image

import com.example.gh_coursework.domain.entity.RouteImageDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetRouteImagesUseCaseImpl(private val repository: TravelRepository): GetRouteImagesUseCase {
    override fun invoke(routeId: String): Flow<List<RouteImageDomain>> {
        return repository.getRouteImages(routeId)
    }
}