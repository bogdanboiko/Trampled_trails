package com.example.gh_coursework.domain.usecase.image

import com.example.gh_coursework.domain.entity.RouteImageDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddRouteImageListUseCaseImpl(private val repository: TravelRepository): AddRouteImageListUseCase {
    override suspend fun invoke(images: List<RouteImageDomain>) = (Dispatchers.IO) {
        repository.addRouteImages(images)
    }
}