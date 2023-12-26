package com.example.cameraproject.domain.models

import com.example.cameraproject.database.objects.DoorObject

data class DoorModel(
    val name: String = "",
    val room: String? = null,
    val id: Int = -1,
    val favorites: Boolean = false,
    val snapshot: String? = null
) {
    fun toDoorObject() = DoorObject(
        name, room, id, favorites, snapshot
    )
}