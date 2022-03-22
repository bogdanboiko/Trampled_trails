package com.example.gh_coursework.domain.usecase.point_preview

import com.example.gh_coursework.domain.entity.PointPreviewDomain

interface AddPointPreviewUseCase {
    suspend fun invoke(point: PointPreviewDomain)
}