package com.example.gh_coursework.data.database.dao

import androidx.room.*
import com.example.gh_coursework.data.database.entity.RouteEntity
import com.example.gh_coursework.data.database.entity.RoutePointEntity
import com.example.gh_coursework.data.database.response.RoutePreviewResponse
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoutePreviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertRoute(routeEntity: RouteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertRoutePointsList(coordinatesList: List<RoutePointEntity>)

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

    @Query("SELECT route_details.*, point_coordinates.* FROM route_details " +
            "INNER JOIN routes_points ON routes_points.parentRouteId == route_details.routeId " +
            "INNER JOIN point_coordinates ON point_coordinates.id == routes_points.pointId")
    abstract fun getRoutesResponse(): Flow<List<RoutePreviewResponse>>
}