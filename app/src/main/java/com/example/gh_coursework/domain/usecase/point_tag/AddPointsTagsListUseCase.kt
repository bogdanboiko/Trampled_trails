package com.example.gh_coursework.domain.usecase.point_tag

import com.example.gh_coursework.domain.entity.PointsTagsDomain

interface AddPointsTagsListUseCase {
    suspend fun invoke(pointsTagsList: List<PointsTagsDomain>)
}