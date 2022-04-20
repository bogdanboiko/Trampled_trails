package com.example.gh_coursework.domain.usecase.point_details

import com.example.gh_coursework.domain.entity.PointCompleteDetailsDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetAllPointsDetailsImpl(private val repository: TravelRepository) : GetAllPointsDetails {
    override fun invoke(): Flow<List<PointCompleteDetailsDomain>> {
        return repository.getAllPointsDetails()
    }
}