package com.example.gh_coursework.domain.usecase.deleted

import com.example.gh_coursework.domain.entity.DeletedPointDomain

interface AddDeletedPointUseCase {
    suspend fun invoke(point: DeletedPointDomain)
}