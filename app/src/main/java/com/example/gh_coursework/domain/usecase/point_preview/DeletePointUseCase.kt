package com.example.gh_coursework.domain.usecase.point_preview

import com.example.gh_coursework.domain.entity.PointDetailsDomain

interface DeletePointUseCase {
    suspend fun invoke(point: PointDetailsDomain)
}