package com.example.gh_coursework.domain.usecase.main_activity

import com.example.gh_coursework.domain.entity.PointDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetAllPointsUseCaseImpl(private val repository: TravelRepository): GetAllPointsUseCase {
    override fun invoke(): Flow<List<PointDomain>> {
        return repository.getAllPoints()
    }
}