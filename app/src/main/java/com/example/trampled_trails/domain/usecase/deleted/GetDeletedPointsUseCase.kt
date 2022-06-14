package com.example.trampled_trails.domain.usecase.deleted

import com.example.trampled_trails.domain.entity.DeletedPointDomain
import kotlinx.coroutines.flow.Flow

interface GetDeletedPointsUseCase {
    fun invoke(): Flow<List<DeletedPointDomain>>
}