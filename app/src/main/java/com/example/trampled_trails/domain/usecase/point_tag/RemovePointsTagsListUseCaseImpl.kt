package com.example.trampled_trails.domain.usecase.point_tag

import com.example.trampled_trails.domain.entity.PointsTagsDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class RemovePointsTagsListUseCaseImpl(private val repository: TravelRepository) : RemovePointsTagsListUseCase {
    override suspend fun invoke(pointsTagsList: List<PointsTagsDomain>) = (Dispatchers.IO) {
        repository.removePointsTagsList(pointsTagsList)
    }
}