package com.example.gh_coursework.domain.usecase.point_details

import com.example.gh_coursework.domain.entity.PointDetailsDomain
import com.example.gh_coursework.domain.repository.TravelRepository

class AddPointDetailsUseCaseImpl(private val repository: TravelRepository) :
    AddPointDetailsUseCase {
    override suspend fun invoke(details: PointDetailsDomain) {
        repository.updatePointOfInterestDetails(details)
    }
}