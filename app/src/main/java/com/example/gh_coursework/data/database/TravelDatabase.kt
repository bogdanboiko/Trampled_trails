package com.example.gh_coursework.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gh_coursework.data.database.dao.PointDetailsDao
import com.example.gh_coursework.data.database.dao.PointPreviewDao
import com.example.gh_coursework.data.database.dao.RoutePreviewDao
import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.data.database.entity.PointDetailsEntity
import com.example.gh_coursework.data.database.entity.RouteDetailsEntity
import com.example.gh_coursework.data.database.entity.RoutePointEntity

@Database(entities = [PointCoordinatesEntity::class,
                     PointDetailsEntity::class,
                     RouteDetailsEntity::class,
                     RoutePointEntity::class], version = 2)
abstract class TravelDatabase: RoomDatabase() {
    abstract fun getPointPreviewDao(): PointPreviewDao
    abstract fun getRoutePreviewDao(): RoutePreviewDao
    abstract fun getPointDetailsDao(): PointDetailsDao
}