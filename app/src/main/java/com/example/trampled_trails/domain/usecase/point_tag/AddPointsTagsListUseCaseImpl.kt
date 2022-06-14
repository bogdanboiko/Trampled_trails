package com.example.trampled_trails.domain.usecase.point_tag

import com.example.trampled_trails.domain.entity.PointsTagsDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddPointsTagsListUseCaseImpl(private val repository: TravelRepository) :
    AddPointsTagsListUseCase {
    override suspend fun invoke(pointsTagsList: List<PointsTagsDomain>) = (Dispatchers.IO) {
        repository.addPointsTagsList(pointsTagsList)
    }
}