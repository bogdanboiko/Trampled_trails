package com.example.trampled_trails.data.database.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.trampled_trails.data.database.entity.PointPreviewEntity
import com.example.trampled_trails.data.database.entity.PointDetailsEntity
import com.example.trampled_trails.data.database.response.PointResponse
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointsDao {
    @Insert(onConflict = REPLACE)
    abstract suspend fun addPointPreview(pointCoordinatesEntity: PointPreviewEntity)

    @Insert(onConflict = REPLACE)
    abstract suspend fun addPointDetails(details: PointDetailsEntity)

    @Update(onConflict = REPLACE)
    abstract suspend fun updatePointDetails(details: PointDetailsEntity)

    @Query("SELECT * FROM point_coordinates WHERE pointId = :pointId")
    abstract fun getPointDetails(pointId: String): Flow<PointResponse>

    @Query("SELECT * FROM point_coordinates WHERE isRoutePoint = 0")
    abstract fun getAllPointsDetails(): Flow<List<PointResponse>>

    @Query("SELECT * FROM point_coordinates")
    abstract fun getAllPoints(): Flow<List<PointResponse>>

    @Query("DELETE FROM point_coordinates")
    abstract fun deleteAllPoints()

    @Query("DELETE FROM point_coordinates WHERE pointId = :pointId")
    abstract fun deletePoint(pointId: String)

    @Transaction
    open suspend fun insertPointPreviewAndCreateDetails(point: PointPreviewEntity) {
        addPointPreview(point)
        addPointDetails(PointDetailsEntity(point.pointId, "", ""))
    }
}