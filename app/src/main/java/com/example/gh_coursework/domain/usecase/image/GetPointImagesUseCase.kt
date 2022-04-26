package com.example.gh_coursework.domain.usecase.image

import com.example.gh_coursework.domain.entity.PointImageDomain
import kotlinx.coroutines.flow.Flow

interface GetPointImagesUseCase {
    fun invoke(pointId: String): Flow<List<PointImageDomain>>
}