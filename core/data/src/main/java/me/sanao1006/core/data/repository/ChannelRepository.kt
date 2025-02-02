package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import me.sanao1006.core.model.notes.Channel
import me.sanao1006.core.model.requestbody.channel.ChannelListRequestBody

interface ChannelRepository {
    @POST("api/channels/search")
    suspend fun getChannelList(
        @Body body: ChannelListRequestBody
    ): List<Channel>
}
