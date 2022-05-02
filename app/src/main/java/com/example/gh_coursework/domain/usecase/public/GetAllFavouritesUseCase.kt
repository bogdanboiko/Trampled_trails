package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.data.remote.entity.PublicFavouriteEntity
import kotlinx.coroutines.flow.Flow

interface GetAllFavouritesUseCase {
    fun invoke(): Flow<List<PublicFavouriteEntity>>
}