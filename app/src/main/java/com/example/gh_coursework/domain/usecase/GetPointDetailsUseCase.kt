package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import kotlinx.coroutines.flow.Flow

interface GetPointDetailsUseCase {
    fun invoke(pointId: Int): Flow<PointDetailsDomain?>
}