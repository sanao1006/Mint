package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import me.sanao1006.core.model.requestbody.auth.AppCreateRequestBody
import me.sanao1006.core.model.requestbody.auth.AuthSessionGenerateRequestBody
import me.sanao1006.core.model.requestbody.auth.AuthSessionUserKeyRequestBody
import me.sanao1006.core.model.response.auth.AppCreateResponse
import me.sanao1006.core.model.response.auth.AuthSessionGenerateResponse
import me.sanao1006.core.model.response.auth.AuthSessionUserKeyResponse

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

    @POST("api/auth/session/userkey")
    suspend fun authSessionUserKey(
        @Body authSessionUserKeyRequestBody: AuthSessionUserKeyRequestBody
    ): AuthSessionUserKeyResponse
}
