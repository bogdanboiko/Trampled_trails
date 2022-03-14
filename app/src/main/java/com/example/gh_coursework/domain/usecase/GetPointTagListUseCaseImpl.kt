package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.repository.TravelRepository

class GetPointTagListUseCaseImpl(private val repository: TravelRepository) : GetPointTagListUseCase {
    override fun invoke() = repository.getPointTagList()
}