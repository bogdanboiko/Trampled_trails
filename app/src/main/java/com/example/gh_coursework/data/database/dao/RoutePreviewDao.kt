package com.example.gh_coursework.data.database.dao

import androidx.room.*
import com.example.gh_coursework.data.database.entity.RouteEntity
import com.example.gh_coursework.data.database.entity.RoutePointEntity
import com.example.gh_coursework.data.database.response.RoutePointImageResponse
import com.example.gh_coursework.data.database.response.RoutePointsResponse
import com.example.gh_coursework.data.database.response.RoutePreviewResponse
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoutePreviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertRoute(routeEntity: RouteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertRoutePointsList(coordinatesList: List<RoutePointEntity>)

    @Update
    abstract suspend fun updateRouteDetails(route: RouteEntity)

    @Transaction
    open suspend fun addRoute(
        routeEntity: RouteEntity,
        coordinatesList: List<RoutePointEntity>
    ) {
        val routeId = insertRoute(routeEntity)

        coordinatesList.forEach {
            it.routeId = routeId
        }

        insertRoutePointsList(coordinatesList)
    }

    @Query("SELECT * FROM route_details")
    abstract fun getRoutesResponse(): Flow<List<RoutePreviewResponse>>

    @Query("SELECT * FROM route_details WHERE routeId = :routeId")
    abstract fun getRouteDetails(routeId: Long): Flow<RoutePreviewResponse>

    @Query("SELECT * FROM routes_points WHERE routeId = :routeId")
    abstract fun getRoutePoints(routeId: Long): Flow<List<RoutePointsResponse>>

    @Query("SELECT * FROM routes_points WHERE routeId = :routeId")
    abstract fun getRoutePointsImages(routeId: Long): Flow<List<RoutePointImageResponse>>

    @Delete
    abstract fun deleteRoute(route: RouteEntity)
}