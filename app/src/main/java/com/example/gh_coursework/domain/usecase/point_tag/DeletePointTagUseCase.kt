package com.example.gh_coursework.domain.usecase.point_tag

import com.example.gh_coursework.domain.entity.PointTagDomain

interface DeletePointTagUseCase {
    suspend fun invoke(tag: PointTagDomain)
}