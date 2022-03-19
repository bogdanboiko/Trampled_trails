package com.example.gh_coursework.domain.usecase.point_tag

import com.example.gh_coursework.domain.entity.PointTagDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetPointTagsUseCaseImpl(private val repository: TravelRepository) : GetPointTagsUseCase {
    override fun invoke(pointId: Long): Flow<List<PointTagDomain>> {
        return repository.getPointsTagsList(pointId)
    }
}