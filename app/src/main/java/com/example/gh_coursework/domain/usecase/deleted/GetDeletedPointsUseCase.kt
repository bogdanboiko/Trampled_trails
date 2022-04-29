package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.entity.DeletedPointDomain
import kotlinx.coroutines.flow.Flow

interface GetDeletedPointsUseCase {
    fun invoke(): Flow<List<DeletedPointDomain>>
}