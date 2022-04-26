package com.example.gh_coursework.domain.usecase.point_preview

interface DeletePointUseCase {
    suspend fun invoke(pointId: String)
}