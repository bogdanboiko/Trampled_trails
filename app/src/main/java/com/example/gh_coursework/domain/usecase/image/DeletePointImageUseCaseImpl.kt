package com.example.gh_coursework.domain.usecase.image

import com.example.gh_coursework.domain.entity.PointImageDomain
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class DeletePointImageUseCaseImpl(private val repository: TravelRepository) : DeletePointImageUseCase {
    override suspend fun invoke(image: PointImageDomain) = (Dispatchers.IO) {
        repository.deletePointImage(image)
    }
}