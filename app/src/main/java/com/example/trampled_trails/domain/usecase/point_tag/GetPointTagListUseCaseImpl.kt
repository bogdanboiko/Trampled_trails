package com.example.trampled_trails.domain.usecase.point_tag

import com.example.trampled_trails.domain.repository.TravelRepository

class GetPointTagListUseCaseImpl(private val repository: TravelRepository) : GetPointTagListUseCase {
    override fun invoke() = repository.getPointTagList()
}