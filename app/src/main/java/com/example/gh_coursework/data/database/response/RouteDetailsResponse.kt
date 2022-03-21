package com.example.gh_coursework.data.database.response

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.gh_coursework.data.database.entity.RouteEntity
import com.example.gh_coursework.data.database.entity.RouteTagEntity
import com.example.gh_coursework.data.database.entity.RouteTagsEntity


data class RouteDetailsResponse(
    @Embedded
    val routeDetails: RouteEntity,
    @Relation(
        parentColumn = "routeId",
        entityColumn = "tagId",
        associateBy = Junction(RouteTagsEntity::class)
    )
    val tagList: List<RouteTagEntity>
)