package com.example.gh_coursework.data.database.dao

import androidx.room.*
import com.example.gh_coursework.data.database.entity.PointTagEntity
import com.example.gh_coursework.data.database.entity.PointsTagsEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointTagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun addTag(tag: PointTagEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun addTagToPoint(pointTag: PointsTagsEntity)

    @Query("SELECT * FROM point_tag")
    abstract fun getPointTags(): Flow<List<PointTagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addTagsToPoint(pointsTagsList: List<PointsTagsEntity>)

    @Delete
    abstract fun deleteTagsFromPoint(pointsTagsList: List<PointsTagsEntity>)

    @Delete
    abstract fun deleteTag(tag: PointTagEntity)
}