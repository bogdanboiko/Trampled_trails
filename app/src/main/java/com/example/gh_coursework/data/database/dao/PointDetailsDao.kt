package com.example.gh_coursework.data.database.dao

import androidx.room.*
import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import com.example.gh_coursework.data.database.entity.PointDetailsEntity
import com.example.gh_coursework.data.database.response.PointDetailsResponse
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addPointPreview(pointCoordinatesEntity: PointCoordinatesEntity): Long

    @Query("DELETE FROM point_coordinates WHERE pointId = :pointId")
    abstract fun deletePoint(pointId: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addPointDetails(details: PointDetailsEntity): Long

    @Update
    abstract suspend fun updatePointDetails(details: PointDetailsEntity)

    @Query("SELECT * FROM point_coordinates WHERE pointId = :pointId")
    abstract fun getPointDetails(pointId: Long): Flow<PointDetailsResponse>

    @Query("SELECT * FROM point_coordinates WHERE isRoutePoint = 0")
    abstract fun getAllPointsDetails(): Flow<List<PointDetailsResponse>>

    @Transaction
    open suspend fun insertPointCoordinatesAndCreateDetails(point: PointCoordinatesEntity): Long {
        val pointId = addPointPreview(point)
        addPointDetails(PointDetailsEntity(pointId, "", ""))
        return pointId
    }
}