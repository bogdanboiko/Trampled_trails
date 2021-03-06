package com.example.trampled_trails.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import org.koin.dsl.module

val networkModule = module {
    single {
        Firebase.firestore
    }

    single {
        FirebaseStorage.getInstance()
    }
}
