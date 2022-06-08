package com.example.gh_coursework.domain.usecase.public

import kotlinx.coroutines.flow.Flow

interface GetUserFavouriteRoutesUseCase {
    fun invoke(userId: String): Flow<List<String>>
}