package com.example.gh_coursework.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gh_coursework.data.database.dao.*
import com.example.gh_coursework.data.database.entity.*

@Database(
    entities = [
        PointCoordinatesEntity::class,
        PointDetailsEntity::class,
        PointTagEntity::class,
        PointsTagsEntity::class,

        RouteEntity::class,
        RoutePointEntity::class,
        RouteTagEntity::class,
        RouteTagsEntity::class], version = 12
)
abstract class TravelDatabase : RoomDatabase() {
    abstract fun getPointPreviewDao(): PointPreviewDao
    abstract fun getPointDetailsDao(): PointDetailsDao
    abstract fun getPointTagDao(): PointTagDao
    abstract fun getRoutePreviewDao(): RoutePreviewDao
    abstract fun getRouteTagDao(): RouteTagDao
}