package com.nvmsolutions.logincompose.data.network

import com.nvmsolutions.logincompose.data.model.LoginRequest
import com.nvmsolutions.logincompose.data.model.LoginResponse
import com.nvmsolutions.logincompose.data.model.RegisterRequest
import com.nvmsolutions.logincompose.data.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("api/v1/users/create")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse
}