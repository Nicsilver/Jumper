package com.github.nicsilver.jumpertest.listeners

import com.github.nicsilver.jumpertest.action.JumpManager
import com.github.nicsilver.jumpertest.action.JumperState
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

class ManagerListener : ProjectManagerListener, DumbAware {
    @Deprecated("Deprecated in Java")
    override fun projectOpened(project: Project) {
        JumperState.getInstance().jumpCaretActions.forEach { jumpAmount ->
            JumpManager.registerJumpAction(jumpAmount)
        }
    }
}
