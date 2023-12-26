package com.example.cameraproject.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameraproject.data.models.ResponseCameraModel
import com.example.cameraproject.data.models.ResponseDoorModel
import com.example.cameraproject.data.models.toListCameraModel
import com.example.cameraproject.data.models.toListDoorModel
import com.example.cameraproject.database.DatabaseEntity
import com.example.cameraproject.database.objects.toListCameraModel
import com.example.cameraproject.database.objects.toListDoorModel
import com.example.cameraproject.domain.events.MainEvents
import com.example.cameraproject.domain.models.CameraModel
import com.example.cameraproject.domain.models.DoorModel
import com.example.cameraproject.domain.repository.MainRepository
import com.example.cameraproject.domain.state.MainState
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainViewModel : ViewModel(),KoinComponent {

    companion object {
        const val TAG = "MainViewModel"
    }

    private val repository: MainRepository = get()
    private val database: DatabaseEntity = get()

    /*
    * Вместо LiveData использовал StateFlow
    * */

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i(TAG,"throwed ${throwable.message}")
        _state.update { it.copy(isRefreshing = false) }
    }

    init {
        viewModelScope.launch {
           if(fetchDatabase()) {
                fetchCameras()
                fetchDoors()
            }
        }
    }

    fun onEvent(event: MainEvents) {
        when(event) {
            is MainEvents.OnCameraFavouriteChange -> {
                _state.update {
                    it.copy(
                        cameraList = state.value.cameraList.toMutableList().also { list ->
                            list[event.index] = list[event.index].copy(favorites = event.newData)
                        }
                    )
                }
                database.updateFavouriteCamera(
                    itemId = state.value.cameraList[event.index].id,
                    newData = event.newData
                )
            }
            is MainEvents.OnDoorFavouriteChange -> {
                _state.update {
                    it.copy(
                        doorsList = state.value.doorsList.toMutableList().also { list ->
                            list[event.index] = list[event.index].copy(favorites = event.newData)
                        }
                    )
                }
                database.updateFavouriteDoor(
                    itemId = state.value.doorsList[event.index].id,
                    newData = event.newData
                )
            }
            is MainEvents.OnDoorNameChange -> {
                _state.update {
                    it.copy(
                        doorsList = state.value.doorsList.toMutableList().also { list ->
                            list[event.index] = list[event.index].copy(name = event.newName)
                        }
                    )
                }
                database.updateNameDoor(
                    itemId = state.value.doorsList[event.index].id,
                    newName = event.newName
                )
            }
        }
    }

    fun del() {
        database.deleteDoors()
        database.deleteCameras()
    }

    fun refreshDoors() {
        _state.update { it.copy(isRefreshing = true) }
        fetchDoors()
    }

    fun refreshCameras() {
        _state.update { it.copy(isRefreshing = true) }
        fetchCameras()
    }


    private fun fetchDatabase(): Boolean {
        val cameras = database.getCameras()
        val doors = database.getDoors()
        if(cameras.isNotEmpty()) {
            _state.update {
                it.copy(cameraList = cameras.toListCameraModel())
            }
        }
        if(doors.isNotEmpty()) {
            _state.update {
                it.copy(doorsList = doors.toListDoorModel())
            }
        }
        return cameras.isEmpty() && doors.isEmpty()
    }

    private fun addToDatabase(item: CameraModel) {
        database.addCamera(item.toCameraObject())
    }

    private fun addToDatabase(item: DoorModel) {
        database.addDoor(item.toDoorObject())
    }


    private fun fetchCameras() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = repository.getCameras()
            val body = response.body() as ResponseCameraModel
            val cameras = body.data.cameras.toListCameraModel()
            _state.update {
                it.copy(
                    status = response.status.value,
                    cameraList = cameras,
                    cameraRooms = body.data.room,
                    isRefreshing = false
                )
            }
            cameras.forEach { addToDatabase(it) }
        }
    }

    private fun fetchDoors() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = repository.getDoors()
            val body = response.body() as ResponseDoorModel
            val doors = body.data.toListDoorModel()
             _state.update {
                it.copy(
                    status = response.status.value,
                    doorsList = doors,
                    isRefreshing = false
                )
            }
            doors.forEach { addToDatabase(it) }
        }
    }

}