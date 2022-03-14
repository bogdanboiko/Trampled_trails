package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.entity.PointTagDomain
import kotlinx.coroutines.flow.Flow

interface GetPointTagListUseCase {
    fun invoke(): Flow<List<PointTagDomain>>
}