package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.entity.DeletedRouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddDeletedRouteUseCaseImpl(private val repository: TravelRepository): AddDeletedRouteUseCase {
    override suspend fun invoke(route: DeletedRouteDomain) = (Dispatchers.IO) {
        repository.addDeletedRoute(route)
    }
}