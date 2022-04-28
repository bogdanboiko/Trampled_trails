package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.entity.DeletedRouteDomain

interface AddDeletedRouteUseCase {
    suspend fun invoke(route: DeletedRouteDomain)
}