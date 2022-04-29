package com.example.gh_coursework.data.database.mapper.deleted

import com.example.gh_coursework.data.database.entity.DeletedPointsEntity
import com.example.gh_coursework.domain.entity.DeletedPointDomain

fun mapDeletedPointEntityToDomain(point: DeletedPointsEntity): DeletedPointDomain {
    return DeletedPointDomain(point.pointId)
}