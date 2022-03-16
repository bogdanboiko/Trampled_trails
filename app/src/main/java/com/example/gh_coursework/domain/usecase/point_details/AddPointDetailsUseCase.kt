package com.example.gh_coursework.domain.usecase.point_details

import com.example.gh_coursework.domain.entity.PointDetailsDomain

interface AddPointDetailsUseCase {
    suspend fun invoke(details: PointDetailsDomain)
}