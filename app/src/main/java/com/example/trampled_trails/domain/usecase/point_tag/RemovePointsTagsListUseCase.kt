package com.example.trampled_trails.domain.usecase.point_tag

import com.example.trampled_trails.domain.entity.PointsTagsDomain

interface RemovePointsTagsListUseCase {
    suspend fun invoke(pointsTagsList: List<PointsTagsDomain>)
}