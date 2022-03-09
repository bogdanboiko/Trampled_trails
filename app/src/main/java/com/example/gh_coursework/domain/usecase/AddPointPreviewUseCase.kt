package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.entity.PointPreviewDomain

interface AddPointPreviewUseCase {
    suspend fun invoke(point: PointPreviewDomain)
}