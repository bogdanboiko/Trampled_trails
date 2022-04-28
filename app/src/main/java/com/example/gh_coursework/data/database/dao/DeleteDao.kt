package com.example.gh_coursework.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.gh_coursework.data.database.entity.DeletedPointsEntity
import com.example.gh_coursework.data.database.entity.DeletedRoutesEntity

@Dao
abstract class DeleteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addDeletedPoint(point: DeletedPointsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addDeletedRoute(route: DeletedRoutesEntity)
}