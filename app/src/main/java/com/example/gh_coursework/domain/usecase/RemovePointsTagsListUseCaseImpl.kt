package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.entity.PointsTagsDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class RemovePointsTagsListUseCaseImpl(private val repository: TravelRepository) :
    RemovePointsTagsListUseCase {
    override suspend fun invoke(pointsTagsList: List<PointsTagsDomain>) {
        repository.removePointsTagsList(pointsTagsList)
    }
}