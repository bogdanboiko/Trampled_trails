package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.entity.DeletedRouteDomain
import kotlinx.coroutines.flow.Flow

interface GetDeletedRoutesUseCase {
    fun invoke(): Flow<List<DeletedRouteDomain>>
}