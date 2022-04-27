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
    abstract fun getPointImages(pointId: String): Flow<List<PointImageEntity>>

    @Query("SELECT * FROM route_image WHERE routeId =:routeId")
    abstract fun getRouteImages(routeId: String): Flow<List<RouteImageEntity>>

    @Delete
    abstract fun deletePointImage(image: PointImageEntity)

    @Query("DELETE FROM route_image WHERE routeId = :routeId AND isUploaded = 0")
    abstract fun deleteAllRouteLocalStoredImages(routeId: String)

    @Query("DELETE FROM point_image WHERE pointId = :pointId AND isUploaded = 0")
    abstract fun deleteAllPointLocalStoredImages(pointId: String)

    @Delete
    abstract fun deleteRouteImage(image: RouteImageEntity)
}