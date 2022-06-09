package com.example.trampled_trails.domain.usecase.image

import com.example.trampled_trails.domain.entity.PointImageDomain

interface DeletePointImageUseCase {
    suspend fun invoke(image: PointImageDomain)
}