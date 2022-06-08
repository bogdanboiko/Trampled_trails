package com.example.gh_coursework.domain.usecase.deleted

import kotlinx.coroutines.flow.Flow

interface GetDeletedImagesUseCase {
    fun invoke(): Flow<List<String>>
}