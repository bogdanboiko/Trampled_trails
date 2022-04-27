package com.example.gh_coursework.domain.usecase.public

import com.example.gh_coursework.domain.repository.TravelRepository

class MakePrivateRoutePublicUseCaseImpl(private val repository: TravelRepository) : MakePrivateRoutePublicUseCase {
    override fun invoke(routeId: String) {
        repository.makePrivateRoutePublic(routeId)
    }
}