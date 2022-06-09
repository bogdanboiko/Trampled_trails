package com.example.trampled_trails.domain.usecase.public

import com.example.trampled_trails.domain.repository.TravelRepository
import kotlinx.coroutines.flow.Flow

class GetUserFavouriteRoutesUseCaseImpl(private val repository: TravelRepository) :
    GetUserFavouriteRoutesUseCase {
    override fun invoke(userId: String): Flow<List<String>> {
        return repository.getUserFavouriteRoutes(userId)
    }
}