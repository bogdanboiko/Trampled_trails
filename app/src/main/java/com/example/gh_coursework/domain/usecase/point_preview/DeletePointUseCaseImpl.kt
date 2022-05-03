package com.example.gh_coursework.domain.usecase.point_preview

import com.example.gh_coursework.domain.entity.DeletedPointDomain
import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class DeletePointUseCaseImpl(private val repository: TravelRepository) : DeletePointUseCase {
    override suspend fun invoke(point: PointDetailsDomain) {
        repository.deletePoint(point)
        repository.addDeletedPoint(DeletedPointDomain(point.pointId))
    }
}