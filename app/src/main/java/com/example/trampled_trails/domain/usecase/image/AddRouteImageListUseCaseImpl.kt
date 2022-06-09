package com.example.trampled_trails.domain.usecase.image

import com.example.trampled_trails.domain.entity.RouteImageDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddRouteImageListUseCaseImpl(private val repository: TravelRepository): AddRouteImageListUseCase {
    override suspend fun invoke(images: List<RouteImageDomain>) = (Dispatchers.IO) {
        repository.addRouteImages(images)
    }
}