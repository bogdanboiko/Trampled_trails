package com.example.trampled_trails.domain.usecase.main_activity

import com.example.trampled_trails.domain.entity.PointDomain
import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetAllPointsUseCaseImpl(private val repository: TravelRepository): GetAllPointsUseCase {
    override fun invoke(): Flow<List<PointDomain>> {
        return repository.getAllPoints()
    }
}