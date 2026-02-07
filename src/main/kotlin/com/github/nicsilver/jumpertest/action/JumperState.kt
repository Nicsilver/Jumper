package com.github.nicsilver.jumpertest.action

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "com.github.nicsilver.jumpertest.action.JumperState",
    storages = [Storage("SdkSettingsPlugin.xml")]
)
class JumperState : PersistentStateComponent<JumperState> {
    var jumpCaretActions: MutableList<Int> = mutableListOf()

    override fun getState(): JumperState = this

    override fun loadState(state: JumperState) {
        jumpCaretActions = state.jumpCaretActions
    }

    companion object {
        fun getInstance(): JumperState =
            ApplicationManager.getApplication().getService(JumperState::class.java)
    }
}
