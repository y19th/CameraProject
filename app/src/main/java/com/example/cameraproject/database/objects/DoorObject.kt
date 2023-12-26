package com.example.cameraproject.database.objects

import com.example.cameraproject.domain.models.DoorModel
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

@Suppress("unused", "PropertyName")
class DoorObject() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var room: String? = null
    var id: Int = -1
    var favorites: Boolean = false
    var snapshot: String? = null

    fun toDoorModel() = DoorModel(
        name = name,
        room = room,
        id = id,
        favorites = favorites,
        snapshot = snapshot,
    )

    constructor(
        name: String = "",
        room: String? = null,
        id: Int = -1,
        favorites: Boolean = false,
        snapshot: String? = null
    ): this() {
        this.name = name
        this.room = room
        this.id = id
        this.favorites = favorites
        this.snapshot = snapshot
    }

}

fun List<DoorObject>.toListDoorModel(): List<DoorModel> {
    val list = mutableListOf<DoorModel>()
    for(item in this) list.add(item.toDoorModel())
    return list
}