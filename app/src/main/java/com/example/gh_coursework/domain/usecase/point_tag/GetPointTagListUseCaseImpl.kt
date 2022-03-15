package com.example.gh_coursework.domain.usecase.point_tag

import com.example.gh_coursework.domain.repository.TravelRepository

class GetPointTagListUseCaseImpl(private val repository: TravelRepository) :
    GetPointTagListUseCase {
    override fun invoke() = repository.getPointTagList()
}