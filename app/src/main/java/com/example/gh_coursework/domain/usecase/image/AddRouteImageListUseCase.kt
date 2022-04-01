package com.example.gh_coursework.domain.usecase.image

import com.example.gh_coursework.domain.entity.RouteImageDomain

interface AddRouteImageListUseCase {
    suspend fun invoke(images: List<RouteImageDomain>)
}