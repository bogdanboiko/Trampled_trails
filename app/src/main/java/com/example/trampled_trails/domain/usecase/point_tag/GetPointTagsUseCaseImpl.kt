package com.example.trampled_trails.domain.usecase.point_tag

import com.example.trampled_trails.domain.entity.PointTagDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetPointTagsUseCaseImpl(private val repository: TravelRepository) : GetPointTagsUseCase {
    override fun invoke(pointId: String): Flow<List<PointTagDomain>> {
        return repository.getPointsTagsList(pointId)
    }
}