package com.example.gh_coursework.ui.point_details.mapper

import com.example.gh_coursework.domain.entity.PointImageDomain
import com.example.gh_coursework.ui.model.ImageModel.PointImageModel

fun mapPointImageDomainToModel(image: PointImageDomain): PointImageModel {
    return PointImageModel(image.pointId, image.image)
}