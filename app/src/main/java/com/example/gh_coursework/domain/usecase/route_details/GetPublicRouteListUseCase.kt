package com.example.gh_coursework.domain.usecase.route_details

import com.example.gh_coursework.domain.entity.PublicRouteDomain
import kotlinx.coroutines.flow.Flow

interface GetPublicRouteListUseCase {
    fun invoke(): Flow<List<PublicRouteDomain>>
}