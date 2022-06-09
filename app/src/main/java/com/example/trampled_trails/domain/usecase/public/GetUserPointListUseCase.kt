package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.entity.PublicPointDomain
import kotlinx.coroutines.flow.Flow

interface GetUserPointListUseCase {
    fun invoke(userId: String): Flow<List<PublicPointDomain>>
}