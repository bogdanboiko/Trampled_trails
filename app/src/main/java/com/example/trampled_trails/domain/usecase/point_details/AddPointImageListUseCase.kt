package com.example.trampled_trails.domain.usecase.point_details

import com.example.trampled_trails.domain.entity.PointImageDomain

interface AddPointImageListUseCase {
    suspend fun invoke(images: List<PointImageDomain>)
}