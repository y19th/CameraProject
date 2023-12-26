package com.example.cameraproject.data.models

import com.example.cameraproject.domain.models.DoorModel
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDoor(
    val name: String = "",
    val room: String? = null,
    val id: Int = -1,
    val favorites: Boolean = false,
    val snapshot: String? = null
) {
    fun toDoorModel() = DoorModel(name, room, id, favorites, snapshot)
}

fun List<ResponseDoor>.toListDoorModel(): List<DoorModel> {
    val list = mutableListOf<DoorModel>()
    for(item in this) list.add(item.toDoorModel())
    return list
}