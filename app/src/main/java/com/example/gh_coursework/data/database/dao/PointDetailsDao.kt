package com.example.gh_coursework.data.database.dao

import androidx.room.*
import com.example.gh_coursework.data.database.entity.PointDetailsEntity
import com.example.gh_coursework.data.database.response.PointDetailsResponse
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointDetailsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addPointDetails(details: PointDetailsEntity): Long

    @Update
    abstract suspend fun updatePointDetails(details: PointDetailsEntity)

    @Query("SELECT * FROM point_details WHERE pointId = :pointId")
    abstract fun getPointDetails(pointId: Long): Flow<PointDetailsResponse>

    @Transaction
    open suspend fun updateOrInsertPointDetails(details: PointDetailsEntity) {
        if (addPointDetails(details) == -1L) {
            updatePointDetails(details)
        }
    }
}