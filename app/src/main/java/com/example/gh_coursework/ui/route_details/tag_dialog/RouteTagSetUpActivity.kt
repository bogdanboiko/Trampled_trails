package com.example.gh_coursework.ui.route_details.tag_dialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gh_coursework.data.database.TravelDatabase_Impl
import com.example.gh_coursework.data.database.entity.RouteTagEntity
import kotlinx.coroutines.launch

class RouteTagSetUpActivity: AppCompatActivity() {

    private val database = TravelDatabase_Impl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            database.getRouteTagDao().setUpTagsTable(
                listOf(
                    RouteTagEntity(0, "Historical"),
                    RouteTagEntity(1, "Evening walk"),
                    RouteTagEntity(2, "Romantic date"),
                    RouteTagEntity(3, "Beer weekend"),
                    RouteTagEntity(4, "Nature"),
                    RouteTagEntity(5, "Deserted"),
                )
            )
        }
    }
}