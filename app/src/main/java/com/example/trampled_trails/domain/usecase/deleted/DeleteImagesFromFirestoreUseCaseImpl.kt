package com.example.trampled_trails.domain.usecase.deleted

import com.example.trampled_trails.domain.repository.TravelRepository

class DeleteImagesFromFirestoreUseCaseImpl(private val repository: TravelRepository) : DeleteImagesFromFirestoreUseCase {
    override fun invoke(images: List<String>) {
        repository.deleteImagesFromFirebase(images)
    }
}