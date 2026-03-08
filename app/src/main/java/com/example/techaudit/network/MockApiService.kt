package com.example.techaudit.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MockApiService {

    @POST("laboratorios")
    suspend fun crearLaboratorio(
        @Body laboratorio: LaboratorioDto
    ): Response<LaboratorioDto>

    @POST("equipos")
    suspend fun crearEquipo(
        @Body equipo: EquipoDto
    ): Response<EquipoDto>
}