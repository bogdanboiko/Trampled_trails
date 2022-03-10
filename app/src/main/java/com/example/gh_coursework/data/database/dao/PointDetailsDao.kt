package com.example.gh_coursework.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gh_coursework.data.database.entity.PointDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointDetailsDao {
    @Insert
    abstract suspend fun addPointDetails(details: PointDetailsEntity)

    @Query("SELECT * FROM point_details WHERE pointId = :pointId")
    abstract fun getPointDetails(pointId: Int): Flow<PointDetailsEntity>
}