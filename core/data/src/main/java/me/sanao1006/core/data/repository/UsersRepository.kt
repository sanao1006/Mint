package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import me.sanao1006.core.model.common.User
import me.sanao1006.core.model.requestbody.users.UsersShowRequestBody

interface UsersRepository {

    @POST("api/users/show")
    suspend fun getUsersShow(
        @Body body: UsersShowRequestBody
    ): User
}
