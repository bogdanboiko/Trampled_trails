package com.example.gh_coursework.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gh_coursework.data.database.dao.*
import com.example.gh_coursework.data.database.entity.*

@Database(
    entities = [PointCoordinatesEntity::class,
        PointDetailsEntity::class,
        RouteEntity::class,
        RoutePointEntity::class,
        PointTagEntity::class,
        PointsTagsEntity::class,
        PointImageEntity::class], version = 12
)
abstract class TravelDatabase : RoomDatabase() {
    abstract fun getPointPreviewDao(): PointPreviewDao
    abstract fun getRoutePreviewDao(): RoutePreviewDao
    abstract fun getPointDetailsDao(): PointDetailsDao
    abstract fun getTagDao(): TagDao
    abstract fun getImageDao(): ImageDao
}