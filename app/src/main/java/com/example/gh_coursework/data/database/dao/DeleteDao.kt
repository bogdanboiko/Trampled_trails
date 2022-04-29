package com.example.gh_coursework.data.database.dao

import androidx.room.*
import com.example.gh_coursework.data.database.entity.DeletedPointsEntity
import com.example.gh_coursework.data.database.entity.DeletedRoutesEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DeleteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addDeletedPoint(point: DeletedPointsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addDeletedRoute(route: DeletedRoutesEntity)

    @Query("SELECT * FROM deleted_points")
    abstract fun getDeletedPoints(): Flow<List<DeletedPointsEntity>>

    @Query("SELECT * FROM deleted_routes")
    abstract fun getDeletedRoutes(): Flow<List<DeletedRoutesEntity>>

    @Query("DELETE FROM deleted_points")
    abstract fun clearDeletedPointsTable()

    @Query("DELETE FROM deleted_routes")
    abstract fun clearDeletedRoutesTable()
}