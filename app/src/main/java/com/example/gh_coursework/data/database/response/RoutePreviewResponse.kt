package com.example.gh_coursework.data.database.response

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.gh_coursework.data.database.entity.*

data class RoutePreviewResponse(
    @Embedded
    val route: RouteEntity,

    @Relation(
        parentColumn = "routeId",
        entityColumn = "tagId",
        associateBy = Junction(RouteTagsEntity::class)
    )
    val tagList: List<RouteTagEntity>,

    @Relation(
        parentColumn = "routeId",
        entityColumn = "routeId",
    )
    val imageList: List<RouteImageEntity>
)