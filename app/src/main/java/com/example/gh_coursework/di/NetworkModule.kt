package com.example.gh_coursework.di

import com.google.firebase.firestore.FirebaseFirestore
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

    single {
        get<FirebaseFirestore>().collection("routes").whereEqualTo("public", true).limit(10)
    }
}
