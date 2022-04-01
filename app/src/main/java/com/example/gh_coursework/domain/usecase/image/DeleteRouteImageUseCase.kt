package com.example.gh_coursework.domain.usecase.image

import com.example.gh_coursework.domain.entity.RouteImageDomain

interface DeleteRouteImageUseCase {
    suspend fun invoke(image: RouteImageDomain)
}