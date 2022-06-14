package com.example.trampled_trails.data.database.mapper.images

import com.example.trampled_trails.data.database.entity.PointImageEntity
import com.example.trampled_trails.domain.entity.PointImageDomain

fun mapPointImageDomainToEntity(image: PointImageDomain): PointImageEntity {
    return PointImageEntity(image.pointId, image.image, image.isUploaded)
}