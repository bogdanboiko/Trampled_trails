package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.entity.PointDetailsDomain

interface AddPointDetailsUseCase {
    suspend fun invoke(details: PointDetailsDomain)
}