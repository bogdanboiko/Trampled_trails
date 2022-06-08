package com.example.gh_coursework.data.database.mapper.deleted

import com.example.gh_coursework.data.database.entity.DeletedPointsEntity
import com.example.gh_coursework.domain.entity.DeletedPointDomain

fun mapDeletedPointDomainToEntity(point: DeletedPointDomain): DeletedPointsEntity {
    return DeletedPointsEntity(point.pointId)
}