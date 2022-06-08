package com.example.gh_coursework.data.database.mapper.deleted

import com.example.gh_coursework.data.database.entity.DeletedRoutesEntity
import com.example.gh_coursework.domain.entity.DeletedRouteDomain

fun mapDeletedRouteEntityToDomain(route: DeletedRoutesEntity): DeletedRouteDomain {
    return DeletedRouteDomain(route.routeId)
}