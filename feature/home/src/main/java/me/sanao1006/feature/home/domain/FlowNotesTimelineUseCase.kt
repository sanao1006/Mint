package me.sanao1006.feature.home.domain

import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.datastore.DataStoreRepository
import javax.inject.Inject

class FlowNotesTimelineUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val dataStoreRepository: DataStoreRepository
) {
}