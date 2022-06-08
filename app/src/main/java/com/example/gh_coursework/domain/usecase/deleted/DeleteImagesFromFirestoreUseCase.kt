package com.example.gh_coursework.domain.usecase.deleted

interface DeleteImagesFromFirestoreUseCase {
    operator fun invoke(images: List<String>)
}