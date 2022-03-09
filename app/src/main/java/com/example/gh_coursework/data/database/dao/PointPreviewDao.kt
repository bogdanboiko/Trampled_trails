package com.example.gh_coursework.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gh_coursework.data.database.entity.PointCoordinatesEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointPreviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addPointPreview(pointCoordinatesEntity: PointCoordinatesEntity)

    @Query("SELECT * FROM point_coordinates")
    abstract fun getPointPreview() : Flow<List<PointCoordinatesEntity>>
}