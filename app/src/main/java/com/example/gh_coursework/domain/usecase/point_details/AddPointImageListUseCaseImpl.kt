package com.example.gh_coursework.domain.usecase.point_details

import com.example.gh_coursework.domain.entity.PointImageDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class AddPointImageListUseCaseImpl(private val repository: TravelRepository) : AddPointImageListUseCase {
    override suspend fun invoke(images: List<PointImageDomain>) {
        repository.addPointImages(images)
    }
}