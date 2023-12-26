package com.example.cameraproject.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDataModel(
    val room: List<String> = listOf(),
    val cameras: List<ResponseCamera> = listOf()
)