package com.example.gh_coursework.domain.usecase.image

import com.example.gh_coursework.domain.entity.RouteImageDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class DeleteRouteImageUseCaseImpl(private val repository: TravelRepository): DeleteRouteImageUseCase {
    override suspend fun invoke(image: RouteImageDomain) = (Dispatchers.IO) {
        repository.deleteRouteImage(image)
    }
}