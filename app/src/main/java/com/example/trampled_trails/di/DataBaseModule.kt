package com.example.trampled_trails.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trampled_trails.data.database.TravelDatabase
import com.example.trampled_trails.ui.data.pointTags
import com.example.trampled_trails.ui.data.routeTags
import org.koin.dsl.module

val localDataBaseModule = module {
    single {
        Room.databaseBuilder(get(), TravelDatabase::class.java, "Travel.db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    routeTags.forEachIndexed { index, s ->
                        db.execSQL("INSERT OR REPLACE INTO route_tag VALUES " +
                                "(${index + 1}, \"${s}\")")
                    }

                    pointTags.forEachIndexed { index, s ->
                        db.execSQL("INSERT OR REPLACE INTO point_tag VALUES " +
                                "(${index + 1}, \"${s}\")")
                    }

                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        get<TravelDatabase>().getRoutePreviewDao()
    }

    single {
        get<TravelDatabase>().getPointDetailsDao()
    }

    single {
        get<TravelDatabase>().getPointTagDao()
    }

    single {
        get<TravelDatabase>().getRouteTagDao()
    }

    single {
        get<TravelDatabase>().getImageDao()
    }

    single {
        get<TravelDatabase>().getDeleteDao()
    }
}