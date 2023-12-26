package com.example.cameraproject.data.models

import kotlinx.serialization.Serializable


@Serializable
data class ResponseCameraModel(
    val success: Boolean = false,
    val data: ResponseDataModel = ResponseDataModel(),
)