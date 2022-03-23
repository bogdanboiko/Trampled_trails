package com.example.gh_coursework.data.database.dao

import androidx.room.*
import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointPreviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addPointPreview(pointCoordinatesEntity: PointCoordinatesEntity): Long

    @Query("SELECT * FROM point_coordinates WHERE isRoutePoint = 0")
    abstract fun getPointPreview() : Flow<List<PointCoordinatesEntity>>

    @Query("DELETE FROM point_coordinates WHERE pointId = :pointId")
    abstract fun deletePoint(pointId: Long)
}