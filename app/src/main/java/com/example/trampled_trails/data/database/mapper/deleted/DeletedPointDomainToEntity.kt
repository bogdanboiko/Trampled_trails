package com.example.trampled_trails.data.database.mapper.deleted

import com.example.trampled_trails.data.database.entity.DeletedPointsEntity
import com.example.trampled_trails.domain.entity.DeletedPointDomain

fun mapDeletedPointDomainToEntity(point: DeletedPointDomain): DeletedPointsEntity {
    return DeletedPointsEntity(point.pointId)
}