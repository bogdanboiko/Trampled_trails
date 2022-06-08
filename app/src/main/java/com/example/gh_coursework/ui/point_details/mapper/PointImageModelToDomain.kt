package com.example.gh_coursework.ui.point_details.mapper

import com.example.gh_coursework.domain.entity.PointImageDomain
import com.example.gh_coursework.ui.private_image_details.model.ImageModel.PointImageModel

fun mapPointImageModelToDomain(image: PointImageModel): PointImageDomain {
    return PointImageDomain(image.pointId, image.image, image.isUploaded)
}