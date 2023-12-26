package com.example.cameraproject.data.models

import com.example.cameraproject.domain.models.CameraModel
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCamera(
    val name: String = "",
    val snapshot: String = "",
    val room: String? = null,
    val id: Int = -1,
    val favorites: Boolean = false,
    val rec: Boolean = false
) {
    fun toCameraModel() = CameraModel(name, snapshot, room, id, favorites, rec)
}

fun List<ResponseCamera>.toListCameraModel(): List<CameraModel> {
    val list = mutableListOf<CameraModel>()
    for(item in this) list.add(item.toCameraModel())
    return list
}