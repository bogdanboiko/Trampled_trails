package com.example.gh_coursework.domain.usecase

interface DeletePointUseCase {
    suspend fun invoke(pointId: Int)
}