package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.data.remote.entity.PublicFavouriteEntity
import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetAllFavouritesUseCaseImpl(private val repository: TravelRepository): GetAllFavouritesUseCase {
    override fun invoke(): Flow<List<PublicFavouriteEntity>> {
        return repository.getAllFavourites()
    }
}