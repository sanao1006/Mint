package me.sanao1006.core.domain.channel

import me.sanao1006.core.data.repository.ChannelRepository
import me.sanao1006.core.model.notes.Channel
import me.sanao1006.core.model.requestbody.channel.ChannelListRequestBody
import javax.inject.Inject

class GetChannelListUseCase @Inject constructor(
    private val channelRepository: ChannelRepository
) {
    suspend operator fun invoke(
        query: String
    ): List<Channel> {
        return channelRepository.getChannelList(
            body = ChannelListRequestBody(
                query = query
            )
        )
    }
}
