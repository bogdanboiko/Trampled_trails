package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import kotlinx.coroutines.flow.Flow

interface GetPointsPreviewUseCase {
    fun invoke() : Flow<List<PointPreviewDomain>>
}