package com.example.gh_coursework.domain.usecase.point_tag

import com.example.gh_coursework.domain.entity.PointTagDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class DeletePointTagUseCaseImpl(private val repository: TravelRepository) : DeletePointTagUseCase {
    override suspend fun invoke(tag: PointTagDomain) {
        repository.deletePointTag(tag)
    }
}