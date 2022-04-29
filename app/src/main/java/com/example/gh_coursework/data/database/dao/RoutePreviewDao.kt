package com.example.gh_coursework.data.database.dao

import androidx.room.*
import com.example.gh_coursework.data.database.entity.RouteEntity
import com.example.gh_coursework.data.database.response.PointResponse
import com.example.gh_coursework.data.database.response.RoutePointImageResponse
import com.example.gh_coursework.data.database.response.RoutePreviewResponse
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoutePreviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertRoute(routeEntity: RouteEntity)

    @Update
    abstract suspend fun updateRouteDetails(route: RouteEntity)

    @Query("SELECT * FROM route_details")
    abstract fun getRoutesResponse(): Flow<List<RoutePreviewResponse>>

    @Query("SELECT * FROM route_details WHERE isPublic = 1")
    abstract fun getPublicRoutesResponse(): Flow<List<RoutePreviewResponse>>

    @Query("SELECT * FROM route_details WHERE routeId = :routeId")
    abstract fun getRouteDetails(routeId: String): Flow<RoutePreviewResponse>

    @Query("SELECT * FROM point_coordinates WHERE routeId = :routeId")
    abstract fun getRoutePoints(routeId: String): Flow<List<PointResponse>>

    @Query("SELECT * FROM point_coordinates WHERE routeId = :routeId")
    abstract fun getRoutePointsImages(routeId: String): Flow<List<RoutePointImageResponse>>

    @Query("UPDATE route_details SET isPublic = 1 WHERE routeId = :routeId")
    abstract fun makePrivateRoutePublic(routeId: String)

    @Query("DELETE FROM route_details")
    abstract fun deleteAllRoutes()

    @Delete
    abstract fun deleteRoute(route: RouteEntity)
}