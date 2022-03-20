package com.example.gh_coursework.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.gh_coursework.data.database.entity.PointImageEntity

@Dao
abstract class ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addPointImages(image: List<PointImageEntity>)
}