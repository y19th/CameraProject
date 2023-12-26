package com.example.cameraproject.domain.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json

class MainRepository {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }


    suspend fun getCameras(): HttpResponse {
        return HttpClient().use {
            client.get("https://cars.cprogroup.ru/api/rubetek/cameras/")
        }
    }
    suspend fun getDoors() : HttpResponse {
        return HttpClient().use {
            client.get("https://cars.cprogroup.ru/api/rubetek/doors/")
        }
    }
}