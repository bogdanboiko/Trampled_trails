package com.example.trampled_trails.data.database.dao

import androidx.room.*
import com.example.trampled_trails.data.database.entity.DeletedImageEntity
import com.example.trampled_trails.data.database.entity.DeletedPointsEntity
import com.example.trampled_trails.data.database.entity.DeletedRoutesEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DeleteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addDeletedPoint(point: DeletedPointsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addDeletedRoute(route: DeletedRoutesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addDeletedImage(image: DeletedImageEntity)

    @Query("SELECT * FROM deleted_points")
    abstract fun getDeletedPoints(): Flow<List<DeletedPointsEntity>>

    @Query("SELECT * FROM deleted_routes")
    abstract fun getDeletedRoutes(): Flow<List<DeletedRoutesEntity>>

    @Query("SELECT * FROM deleted_images")
    abstract fun getDeletedImages(): Flow<List<DeletedImageEntity>>

    @Query("DELETE FROM deleted_points")
    abstract fun clearDeletedPointsTable()

    @Query("DELETE FROM deleted_routes")
    abstract fun clearDeletedRoutesTable()

    @Query("DELETE FROM deleted_images")
    abstract fun clearDeletedImagesTable()
}