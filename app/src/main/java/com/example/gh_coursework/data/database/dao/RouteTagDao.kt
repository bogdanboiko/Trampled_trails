package com.example.gh_coursework.data.database.dao

import androidx.room.*
import com.example.gh_coursework.data.database.entity.RouteTagEntity
import com.example.gh_coursework.data.database.entity.RouteTagsEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RouteTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun setUpTagsTable(tags: List<RouteTagEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addRouteTags(routeTagsList: List<RouteTagsEntity>)

    @Query("SELECT * FROM route_tag")
    abstract fun getTagsList(): Flow<List<RouteTagEntity>>


    @Delete
    abstract fun deleteTagsFromRoute(routeTagsList: List<RouteTagsEntity>)
}