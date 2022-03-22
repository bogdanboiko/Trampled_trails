package com.example.gh_coursework.data.database.dao

import androidx.room.*
import com.example.gh_coursework.data.database.entity.PointImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addPointImages(image: List<PointImageEntity>)

    @Query("SELECT * FROM point_image WHERE pointId =:pointId")
    abstract fun getPointImages(pointId: Long): Flow<List<PointImageEntity>>

    @Delete
    abstract fun deletePointImage(image: PointImageEntity)
}