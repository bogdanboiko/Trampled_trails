package com.example.gh_coursework.domain.usecase.point_details

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import kotlinx.coroutines.flow.Flow

interface GetPointDetailsUseCase {
    fun invoke(pointId: Long): Flow<PointDetailsDomain?>
}