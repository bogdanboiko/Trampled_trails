package com.example.gh_coursework.data.database.mapper

import com.example.gh_coursework.data.database.entity.PointImageEntity
import com.example.gh_coursework.domain.entity.PointImageDomain

fun mapPointImageEntityToDomain(image: PointImageEntity): PointImageDomain {
    return PointImageDomain(image.pointId, image.image)
}