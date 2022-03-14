package com.example.gh_coursework.domain.usecase

import com.example.gh_coursework.domain.entity.PointTagDomain

interface DeletePointTagUseCase {
    suspend fun invoke(tag: PointTagDomain)
}