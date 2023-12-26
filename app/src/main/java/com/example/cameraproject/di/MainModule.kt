package com.example.cameraproject.di

import com.example.cameraproject.database.objects.CameraObject
import com.example.cameraproject.database.DatabaseEntity
import com.example.cameraproject.database.objects.DoorObject
import com.example.cameraproject.domain.repository.MainRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module

val mainModule = module {
    single { MainRepository() }

    single {
        val config = RealmConfiguration
            .Builder(schema = setOf(CameraObject::class, DoorObject::class))
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.open(config)
    }

    single {
        DatabaseEntity()
    }
}