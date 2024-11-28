package me.sanao1006.feature.note

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@CircuitInject(NoteScreen::class, SingletonComponent::class)
class NoteScreenPresenter @Inject constructor() : Presenter<NoteScreen.State> {
    @Composable
    override fun present(): NoteScreen.State {
        return NoteScreen.State
    }
}