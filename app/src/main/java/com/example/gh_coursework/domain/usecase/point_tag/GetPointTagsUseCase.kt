package com.example.gh_coursework.domain.usecase.point_tag

import com.example.gh_coursework.domain.entity.PointTagDomain
import kotlinx.coroutines.flow.Flow

interface GetPointTagsUseCase {
    fun invoke(pointId: Long): Flow<List<PointTagDomain>>
}