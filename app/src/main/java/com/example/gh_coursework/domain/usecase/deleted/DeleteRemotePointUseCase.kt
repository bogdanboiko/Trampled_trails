package com.example.gh_coursework.domain.usecase.deleted

interface DeleteRemotePointUseCase {
    suspend fun invoke(pointId: String)
}