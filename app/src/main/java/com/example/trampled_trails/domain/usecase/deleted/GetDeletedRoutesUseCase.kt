package com.example.trampled_trails.domain.usecase.deleted

import com.example.trampled_trails.domain.entity.DeletedRouteDomain
import kotlinx.coroutines.flow.Flow

interface GetDeletedRoutesUseCase {
    fun invoke(): Flow<List<DeletedRouteDomain>>
}