package com.example.gh_coursework.domain.usecase.image

import com.example.gh_coursework.domain.entity.PointImageDomain

interface AddPointImageListUseCase {
    suspend fun invoke(images: List<PointImageDomain>)
}