package com.example.trampled_trails.data.database.dao

import androidx.room.*
import com.example.trampled_trails.data.database.entity.PointTagEntity
import com.example.trampled_trails.data.database.entity.PointsTagsEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addTagToPoint(pointTag: PointsTagsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addTagsToPoint(pointsTagsList: List<PointsTagsEntity>)

    @Query("SELECT * FROM point_tag")
    abstract fun getPointTags(): Flow<List<PointTagEntity>>

    @Delete
    abstract fun deleteTagsFromPoint(pointsTagsList: List<PointsTagsEntity>)
}