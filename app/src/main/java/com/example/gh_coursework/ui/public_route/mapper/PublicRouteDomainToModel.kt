@file:JvmName("PublicRouteDomainToModelKt")

package com.example.gh_coursework.ui.public_route.mapper

import com.example.gh_coursework.domain.entity.PublicRouteDomain
import com.example.gh_coursework.ui.public_route.model.PublicRouteModel

fun mapPublicRouteDomainToModel(routeDomain: PublicRouteDomain): PublicRouteModel {
    with(routeDomain) {
        return PublicRouteModel(
            documentId,
            name,
            description,
            0.0,
            tagsList,
            imageList
        )
    }
}