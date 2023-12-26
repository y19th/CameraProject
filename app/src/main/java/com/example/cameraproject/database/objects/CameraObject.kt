package com.example.cameraproject.database.objects

import com.example.cameraproject.domain.models.CameraModel
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Required
import org.mongodb.kbson.ObjectId

@Suppress("PropertyName", "MemberVisibilityCanBePrivate", "unused")
class CameraObject() : RealmObject {

    @PrimaryKey var _id: ObjectId = ObjectId()
    @Required var name: String = ""
    var id: Int = -1
    var room: String? = null
    var snapshot: String = ""
    var favorites: Boolean = false
    var rec: Boolean = false

    fun toCameraModel() = CameraModel(
        name = name,
        snapshot = snapshot,
        favorites = favorites,
        rec = rec,
        room = room,
        id = id
    )


    constructor(
        id: Int = -1,
        name: String = "",
        snapshot: String = "",
        favorites: Boolean = false,
        rec: Boolean = false,
        room: String? = null
    ) : this() {
        this.id = id
        this.name = name
        this.snapshot = snapshot
        this.favorites = favorites
        this.rec = rec
        this.room = room
    }
}


fun List<CameraObject>.toListCameraModel(): List<CameraModel> {
    val list = mutableListOf<CameraModel>()
    for(item in this) list.add(item.toCameraModel())
    return list
}