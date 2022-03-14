package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.entity.PointsTagsDomain

interface AddPointsTagsListUseCase {
    suspend fun invoke(pointsTagsList: List<PointsTagsDomain>)
}