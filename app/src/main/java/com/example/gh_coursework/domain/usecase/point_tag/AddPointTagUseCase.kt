package com.example.gh_coursework.domain.usecase.point_tag

import com.example.gh_coursework.domain.entity.PointTagDomain

interface AddPointTagUseCase {
    suspend fun invoke(tag: PointTagDomain)
}