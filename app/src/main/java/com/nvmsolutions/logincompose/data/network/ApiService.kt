package com.nvmsolutions.logincompose.data.network

import com.nvmsolutions.logincompose.data.model.LoginRequest
import com.nvmsolutions.logincompose.data.model.LoginResponse
import com.nvmsolutions.logincompose.data.model.RegisterRequest
import com.nvmsolutions.logincompose.data.model.RegisterResponse
import com.nvmsolutions.logincompose.data.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("api/v1/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("api/v1/users/create")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @PUT("api/v1/users/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Body updateRequest: UpdateUserRequest
    ): User

    @DELETE("api/v1/users/{id}")
    suspend fun deleteUser(@Path("id") userId: String)
}

data class UpdateUserRequest(val name: String)