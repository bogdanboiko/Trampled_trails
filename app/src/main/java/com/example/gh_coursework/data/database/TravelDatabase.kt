package com.example.gh_coursework.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gh_coursework.data.database.dao.*
import com.example.gh_coursework.data.database.entity.*

@Database(
    entities = [
        DeletedImageEntity::class,
        DeletedPointsEntity::class,
        DeletedRoutesEntity::class,
        PointPreviewEntity::class,
        PointDetailsEntity::class,
        PointTagEntity::class,
        PointsTagsEntity::class,
        PointImageEntity::class,
        RouteEntity::class,
        RouteTagEntity::class,
        RouteTagsEntity::class,
        RouteImageEntity::class], version = 23
)
abstract class TravelDatabase : RoomDatabase() {
    abstract fun getRoutePreviewDao(): RoutesDao
    abstract fun getPointDetailsDao(): PointsDao
    abstract fun getPointTagDao(): PointTagDao
    abstract fun getRouteTagDao(): RouteTagDao
    abstract fun getImageDao(): ImageDao
    abstract fun getDeleteDao(): DeleteDao
}