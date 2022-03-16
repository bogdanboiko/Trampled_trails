package com.example.gh_coursework.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gh_coursework.data.database.TravelDatabase
import org.koin.dsl.module

val localDataBaseModule = module {
    single {
        Room.databaseBuilder(get(), TravelDatabase::class.java, "Travel.db")
//            .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    db.execSQL(
//                        """
//                            CREATE TRIGGER IF NOT EXISTS route_details.delete_route
//                            BEFORE DELETE ON route_details
//                            BEGIN DELETE FROM routes_points WHERE routeId = DELETED.routeId; END
//                        """.trimIndent()
//                    )
//                }
//            })
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        get<TravelDatabase>().getPointPreviewDao()
    }

    single {
        get<TravelDatabase>().getRoutePreviewDao()
    }

    single {
        get<TravelDatabase>().getPointDetailsDao()
    }

    single {
        get<TravelDatabase>().getTagDao()
    }
}