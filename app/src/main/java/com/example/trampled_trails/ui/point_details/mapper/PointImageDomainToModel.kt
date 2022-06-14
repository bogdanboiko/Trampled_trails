package com.example.trampled_trails.ui.point_details.mapper

import com.example.trampled_trails.domain.entity.PointImageDomain
import com.example.trampled_trails.ui.private_image_details.model.ImageModel.PointImageModel

fun mapPointImageDomainToModel(image: PointImageDomain): PointImageModel {
    return PointImageModel(image.pointId, image.image, image.isUploaded)
}