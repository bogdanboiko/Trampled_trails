package com.example.gh_coursework.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gh_coursework.data.database.dao.PointDetailsDao
import com.example.gh_coursework.data.database.dao.PointPreviewDao
import com.example.gh_coursework.data.database.dao.RoutePreviewDao
import com.example.gh_coursework.data.database.dao.TagDao
import com.example.gh_coursework.data.database.entity.*

@Database(
    entities = [PointCoordinatesEntity::class,
        PointDetailsEntity::class,
        RouteEntity::class,
        RoutePointEntity::class,
        PointTagEntity::class,
        PointsTagsEntity::class], version = 11
)
abstract class TravelDatabase : RoomDatabase() {
    abstract fun getPointPreviewDao(): PointPreviewDao
    abstract fun getRoutePreviewDao(): RoutePreviewDao
    abstract fun getPointDetailsDao(): PointDetailsDao
    abstract fun getTagDao(): TagDao
}