package com.example.gh_coursework.domain.usecase.image

import com.example.gh_coursework.domain.entity.PointImageDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class AddPointImageListUseCaseImpl(private val repository: TravelRepository) :
    AddPointImageListUseCase {
    override suspend fun invoke(images: List<PointImageDomain>) = (Dispatchers.IO) {
        repository.addPointImages(images)
    }
}