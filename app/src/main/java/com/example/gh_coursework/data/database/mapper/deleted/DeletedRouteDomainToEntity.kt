package com.example.gh_coursework.data.database.mapper.deleted

import com.example.gh_coursework.data.database.entity.DeletedRoutesEntity
import com.example.gh_coursework.domain.entity.DeletedRouteDomain

fun mapDeletedRouteDomainToEntity(route: DeletedRouteDomain): DeletedRoutesEntity {
    return DeletedRoutesEntity(route.routeId)
}