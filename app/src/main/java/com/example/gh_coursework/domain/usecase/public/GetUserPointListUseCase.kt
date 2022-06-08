package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.entity.PublicPointDomain
import kotlinx.coroutines.flow.Flow

interface GetUserPointListUseCase {
    fun invoke(userId: String): Flow<List<PublicPointDomain>>
}