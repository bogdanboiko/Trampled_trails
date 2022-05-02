package com.example.gh_coursework.domain.usecase.route_details

import com.example.gh_coursework.domain.entity.PublicRouteDomain
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetPublicRouteListUseCaseImpl(private val repository: TravelRepository) : GetPublicRouteListUseCase {
    override fun invoke(): Flow<List<PublicRouteDomain>> {
        return repository.getPublicRoutesList()
    }
}