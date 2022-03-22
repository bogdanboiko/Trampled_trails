package com.example.gh_coursework.domain.usecase.image

import com.example.gh_coursework.domain.entity.PointImageDomain

interface DeletePointImageUseCase {
    suspend fun invoke(image: PointImageDomain)
}