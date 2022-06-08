package com.example.gh_coursework.domain.usecase.main_activity

import com.example.gh_coursework.domain.entity.PointDomain
import kotlinx.coroutines.flow.Flow

interface GetAllPointsUseCase {
    fun invoke(): Flow<List<PointDomain>>
}