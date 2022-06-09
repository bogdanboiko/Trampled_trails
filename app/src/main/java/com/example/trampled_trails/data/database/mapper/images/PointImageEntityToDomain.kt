package com.example.trampled_trails.data.database.mapper.images

import com.example.trampled_trails.data.database.entity.PointImageEntity
import com.example.trampled_trails.domain.entity.PointImageDomain

fun mapPointImageEntityToDomain(image: PointImageEntity): PointImageDomain {
    return PointImageDomain(image.pointId, image.image, image.isUploaded)
}