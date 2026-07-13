package com.github.nicsilver.jumpertest.listeners

import com.github.nicsilver.jumpertest.action.JumpManager
import com.github.nicsilver.jumpertest.action.JumperState
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class ManagerListener : ProjectActivity {
    override suspend fun execute(project: Project) {
        val actionManager = ActionManager.getInstance()
        JumperState.getInstance().jumpCaretActions.forEach { jumpAmount ->
            // Jump actions are registered application-wide, so only register once
            // even when several projects are opened during the session.
            if (actionManager.getAction("JumpUp $jumpAmount") == null) {
                JumpManager.registerJumpAction(jumpAmount)
            }
        }
    }
}
