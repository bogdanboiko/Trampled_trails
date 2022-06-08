package com.example.gh_coursework.data.database.mapper.images

import com.example.gh_coursework.data.database.entity.PointImageEntity
import com.example.gh_coursework.domain.entity.PointImageDomain

fun mapPointImageDomainToEntity(image: PointImageDomain): PointImageEntity {
    return PointImageEntity(image.pointId, image.image, image.isUploaded)
}