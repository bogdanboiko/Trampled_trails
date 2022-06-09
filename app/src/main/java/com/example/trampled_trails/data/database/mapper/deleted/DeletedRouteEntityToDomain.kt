package com.example.trampled_trails.data.database.mapper.deleted

import com.example.trampled_trails.data.database.entity.DeletedRoutesEntity
import com.example.trampled_trails.domain.entity.DeletedRouteDomain

fun mapDeletedRouteEntityToDomain(route: DeletedRoutesEntity): DeletedRouteDomain {
    return DeletedRouteDomain(route.routeId)
}