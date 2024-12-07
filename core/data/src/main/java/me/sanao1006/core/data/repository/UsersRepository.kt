package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.POST
import me.sanao1006.core.model.notes.User

interface UsersRepository {

    @POST("api/users/show")
    suspend fun getUsersShow(): User
}