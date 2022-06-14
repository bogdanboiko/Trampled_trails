package com.example.trampled_trails.domain.usecase.deleted

import kotlinx.coroutines.flow.Flow

interface GetDeletedImagesUseCase {
    fun invoke(): Flow<List<String>>
}