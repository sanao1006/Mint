package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import me.sanao1006.core.model.meta.Announcement
import me.sanao1006.core.model.requestbody.meta.MetaAnnouncementsRequestBody

interface MetaRepository {
    @POST("api/announcements")
    suspend fun getAnnouncements(
        @Body body: MetaAnnouncementsRequestBody
    ): List<Announcement>
}
