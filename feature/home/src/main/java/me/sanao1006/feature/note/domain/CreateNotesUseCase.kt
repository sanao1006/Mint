package me.sanao1006.feature.note.domain

import me.sanao1006.core.data.repository.NotesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
}
