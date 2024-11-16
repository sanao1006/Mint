package me.sanao1006.core.network.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import me.sanao1006.core.model.AppCreateRequestBody
import me.sanao1006.core.model.AppCreateResponse
import me.sanao1006.core.model.AuthSessionGenerateRequestBody
import me.sanao1006.core.model.AuthSessionGenerateResponse

interface MiauthRepository {
    @POST("api/miauth/{session}/check")
    suspend fun miauth(@Path("session") session: String): String

    @POST("api/app/create")
    suspend fun createApp(
        @Body appCreateRequestBody: AppCreateRequestBody
    ): AppCreateResponse

    @POST("api/auth/session/generate")
    suspend fun authSessionGenerate(
        @Body authSessionGenerateRequestBody: AuthSessionGenerateRequestBody
    ): AuthSessionGenerateResponse
}