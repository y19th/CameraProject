package com.example.cameraproject.domain.state

import com.example.cameraproject.domain.models.CameraModel
import com.example.cameraproject.domain.models.DoorModel

data class MainState(
    val status: Int = 0,
    val doorsList: List<DoorModel> = listOf(),
    val cameraRooms: List<String> = listOf(),
    val cameraList: List<CameraModel> = listOf(),

    val isRefreshing: Boolean = false
)