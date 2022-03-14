package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.entity.PointsTagsDomain

interface RemovePointsTagsListUseCase {
    suspend fun invoke(pointsTagsList: List<PointsTagsDomain>)
}