package com.example.trampled_trails.domain.usecase.deleted

interface DeleteImagesFromFirestoreUseCase {
    operator fun invoke(images: List<String>)
}