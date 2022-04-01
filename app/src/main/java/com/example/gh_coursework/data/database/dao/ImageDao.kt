package com.example.gh_coursework.data.database.dao

import androidx.room.*
import com.example.gh_coursework.data.database.entity.PointImageEntity
import com.example.gh_coursework.data.database.entity.RouteImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addPointImages(image: List<PointImageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addRouteImages(image: List<RouteImageEntity>)

    @Query("SELECT * FROM point_image WHERE pointId =:pointId")
    abstract fun getPointImages(pointId: Long): Flow<List<PointImageEntity>>

    @Query("SELECT * FROM route_image WHERE routeId =:routeId")
    abstract fun getRouteImages(routeId: Long): Flow<List<RouteImageEntity>>

    @Delete
    abstract fun deletePointImage(image: PointImageEntity)

    @Delete
    abstract fun deleteRouteImage(image: RouteImageEntity)
}