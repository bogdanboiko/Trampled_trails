package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.repository.TravelRepository

class DeleteImagesFromFirestoreUseCaseImpl(private val repository: TravelRepository) : DeleteImagesFromFirestoreUseCase {
    override fun invoke(images: List<String>) {
        repository.deleteImagesFromFirebase(images)
    }
}