package com.github.nicsilver.jumpertest.action

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.extensions.PluginId

object JumpManager {
    private const val PLUGIN_ID = "Jumper"

    fun addJumpAction(jumpAmount: Int) {
        JumperState.getInstance().jumpCaretActions.add(jumpAmount)
        registerJumpAction(jumpAmount)
    }

    fun removeJumpAction(jumpAmount: Int) {
        JumperState.getInstance().jumpCaretActions.remove(jumpAmount)
        unregisterJumpAction(jumpAmount)
    }

    fun registerJumpAction(jumpAmount: Int) {
        val actionManager = ActionManager.getInstance()
        val pluginId = PluginId.getId(PLUGIN_ID)

        val jumpUp = JumpCaretAction(
            "$jumpAmount Jump Up",
            "Jump Up $jumpAmount",
            null,
            jumpAmount,
            JumpCaretAction.EditorAction.UP
        )

        val jumpDown = JumpCaretAction(
            "$jumpAmount Jump Down",
            "Jump Down $jumpAmount",
            null,
            jumpAmount,
            JumpCaretAction.EditorAction.DOWN
        )

        val jumpUpWithSelection = JumpCaretAction(
            "$jumpAmount Jump Up Selection",
            "Jump Up Selection $jumpAmount",
            null,
            jumpAmount,
            JumpCaretAction.EditorAction.UP_WITH_SELECTION
        )

        val jumpDownWithSelection = JumpCaretAction(
            "$jumpAmount Jump Down Selection",
            "Jump Down Selection $jumpAmount",
            null,
            jumpAmount,
            JumpCaretAction.EditorAction.DOWN_WITH_SELECTION
        )

        actionManager.registerAction("JumpUp $jumpAmount", jumpUp, pluginId)
        actionManager.registerAction("JumpDown $jumpAmount", jumpDown, pluginId)
        actionManager.registerAction("JumpUpSelect $jumpAmount", jumpUpWithSelection, pluginId)
        actionManager.registerAction("JumpDownSelect $jumpAmount", jumpDownWithSelection, pluginId)
    }

    fun unregisterJumpAction(jumpAmount: Int) {
        val actionManager = ActionManager.getInstance()
        actionManager.unregisterAction("JumpUp $jumpAmount")
        actionManager.unregisterAction("JumpDown $jumpAmount")
        actionManager.unregisterAction("JumpUpSelect $jumpAmount")
        actionManager.unregisterAction("JumpDownSelect $jumpAmount")
    }
}
