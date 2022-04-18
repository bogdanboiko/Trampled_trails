package com.example.gh_coursework.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gh_coursework.data.database.TravelDatabase
import org.koin.dsl.module

val localDataBaseModule = module {
    single {
        Room.databaseBuilder(get(), TravelDatabase::class.java, "Travel.db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL(
                        """
                            CREATE TRIGGER IF NOT EXISTS delete_point
                            AFTER DELETE ON routes_points
                            BEGIN 
                            DELETE FROM point_coordinates WHERE pointId = OLD.pointId; 
                            END
                        """.trimIndent()
                    )

                    db.execSQL("INSERT OR REPLACE INTO route_tag VALUES " +
                            "(1, \"Historical\"), " +
                            "(2, \"Evening walk\"), " +
                            "(3, \"Romantic date\"), " +
                            "(4, \"Beer weekend\"), " +
                            "(5, \"Nature\"), " +
                            "(6, \"Deserted\")")
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
}