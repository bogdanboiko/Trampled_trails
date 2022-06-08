package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetUserFavouriteRoutesUseCaseImpl(private val repository: TravelRepository) :
    GetUserFavouriteRoutesUseCase {
    override fun invoke(userId: String): Flow<List<String>> {
        return repository.getUserFavouriteRoutes(userId)
    }
}