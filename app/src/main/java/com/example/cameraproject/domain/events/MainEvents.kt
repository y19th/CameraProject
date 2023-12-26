package com.example.cameraproject.domain.events

sealed interface MainEvents {

    data class OnCameraFavouriteChange(val newData: Boolean, val index: Int) : MainEvents

    data class OnDoorFavouriteChange(val newData: Boolean,val index: Int): MainEvents

    data class OnDoorNameChange(val newName: String,val index: Int): MainEvents
}