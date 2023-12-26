package com.example.cameraproject.domain.models

import com.example.cameraproject.database.objects.CameraObject

data class CameraModel(
    val name: String = "",
    val snapshot: String = "",
    val room: String? = null,
    val id: Int = -1,
    val favorites: Boolean = false,
    val rec: Boolean = false
) {
    fun toCameraObject() = CameraObject(
        name = name,
        snapshot = snapshot,
        room = room,
        id = id,
        favorites = favorites,
        rec = rec
    )
}