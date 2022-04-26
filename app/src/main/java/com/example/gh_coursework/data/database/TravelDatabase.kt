package com.example.gh_coursework.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gh_coursework.data.database.dao.*
import com.example.gh_coursework.data.database.entity.*

@Database(
    entities = [PointCoordinatesEntity::class,
        PointDetailsEntity::class,
        PointTagEntity::class,
        PointsTagsEntity::class,
        PointImageEntity::class,
        RouteEntity::class,
        RoutePointEntity::class,
        RouteTagEntity::class,
        RouteTagsEntity::class,
        RouteImageEntity::class], version = 18
)
abstract class TravelDatabase : RoomDatabase() {
    abstract fun getRoutePreviewDao(): RoutePreviewDao
    abstract fun getPointDetailsDao(): PointDetailsDao
    abstract fun getPointTagDao(): PointTagDao
    abstract fun getRouteTagDao(): RouteTagDao
    abstract fun getImageDao(): ImageDao
}