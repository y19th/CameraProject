package com.example.cameraproject.data.models

import kotlinx.serialization.Serializable


@Serializable
data class ResponseDoorModel(
    val success: Boolean = false,
    val data: List<ResponseDoor> = listOf()
)
