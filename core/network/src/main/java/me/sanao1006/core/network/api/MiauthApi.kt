package me.sanao1006.core.network.api

import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface MiauthApi {
    @POST("api/miauth/{session}/check")
    fun miauth(@Path("session") session: String)
}

