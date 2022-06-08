package com.example.gh_coursework.domain.usecase.point_tag

import com.example.gh_coursework.domain.entity.PointsTagsDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddPointsTagsListUseCaseImpl(private val repository: TravelRepository) :
    AddPointsTagsListUseCase {
    override suspend fun invoke(pointsTagsList: List<PointsTagsDomain>) = (Dispatchers.IO) {
        repository.addPointsTagsList(pointsTagsList)
    }
}