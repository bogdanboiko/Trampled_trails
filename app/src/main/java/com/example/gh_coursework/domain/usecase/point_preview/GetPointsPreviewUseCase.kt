package com.example.gh_coursework.domain.usecase.point_preview

import com.example.gh_coursework.domain.entity.PointPreviewDomain
import kotlinx.coroutines.flow.Flow

interface GetPointsPreviewUseCase {
    fun invoke() : Flow<List<PointPreviewDomain>>
}