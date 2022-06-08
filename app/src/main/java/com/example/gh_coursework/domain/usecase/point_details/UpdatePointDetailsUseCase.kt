package com.example.gh_coursework.domain.usecase.point_details

import com.example.gh_coursework.domain.entity.PointDetailsDomain

interface UpdatePointDetailsUseCase {
    suspend fun invoke(details: PointDetailsDomain)
}