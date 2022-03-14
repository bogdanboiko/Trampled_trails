package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.entity.PointTagDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class AddPointTagUseCaseImpl(private val repository: TravelRepository) : AddPointTagUseCase {
    override suspend fun invoke(tag: PointTagDomain) {
        repository.addPointTag(tag)
    }
}