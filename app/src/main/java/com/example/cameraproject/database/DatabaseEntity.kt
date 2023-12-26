package com.example.cameraproject.database

import com.example.cameraproject.database.objects.CameraObject
import com.example.cameraproject.database.objects.DoorObject
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Suppress("unused")
class DatabaseEntity : KoinComponent {

    private val realm: Realm = get()

    fun getCameras(): List<CameraObject> {
        return realm.query<CameraObject>().find().toList()
    }

    fun addCamera(item: CameraObject) {
        realm.writeBlocking {
            val camera = query<CameraObject>("id == $0",item.id).find()

            if(camera.isEmpty()) {
                return@writeBlocking copyToRealm(item)
            } else {
                return@writeBlocking camera.first().also { it.favorites = item.favorites }
            }
        }
    }

    fun getDoors() : List<DoorObject> {
        return realm.query<DoorObject>().find().toList()
    }

    fun addDoor(item: DoorObject) {
        realm.writeBlocking {
            val door = query<DoorObject>("id == $0",item.id).find()

            if(door.isEmpty()) {
                return@writeBlocking copyToRealm(item)
            } else {
                return@writeBlocking door.first().also { it.favorites = item.favorites }
            }
        }
    }

    fun updateFavouriteCamera(itemId: Int, newData: Boolean) {
        realm.writeBlocking {
            val camera = query<CameraObject>("id == $0",itemId).find().first()
            camera.favorites = newData
        }
    }

    fun updateFavouriteDoor(itemId: Int, newData: Boolean) {
        realm.writeBlocking {
            val door = query<DoorObject>("id == $0",itemId).find().first()
            door.favorites = newData
        }
    }

    fun deleteDoors() {
        realm.writeBlocking {
            this.query<DoorObject>().find().forEach { delete(it) }
        }
    }

    fun deleteCameras() {
        realm.writeBlocking {
            this.query<CameraObject>().find().forEach { delete(it) }
        }
    }

}