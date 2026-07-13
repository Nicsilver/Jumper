package com.github.nicsilver.jumpertest.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import javax.swing.Icon
import javax.swing.JList
import javax.swing.JTree

class JumpCaretAction(
    text: String?,
    description: String?,
    icon: Icon?,
    private val jumpAmount: Int,
    private val editorAction: EditorAction
) : AnAction(text, description, icon) {

    enum class EditorAction {
        UP, DOWN, UP_WITH_SELECTION, DOWN_WITH_SELECTION
    }

    private val ideAction: String = when (editorAction) {
        EditorAction.UP -> IdeActions.ACTION_EDITOR_MOVE_CARET_UP
        EditorAction.DOWN -> IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN
        EditorAction.UP_WITH_SELECTION -> IdeActions.ACTION_EDITOR_MOVE_CARET_UP_WITH_SELECTION
        EditorAction.DOWN_WITH_SELECTION -> IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN_WITH_SELECTION
    }

    private val isDown = editorAction == EditorAction.DOWN || editorAction == EditorAction.DOWN_WITH_SELECTION
    private val withSelection = editorAction == EditorAction.UP_WITH_SELECTION || editorAction == EditorAction.DOWN_WITH_SELECTION

    override fun actionPerformed(e: AnActionEvent) {
        // Try editor, then tree, then list
        val editor = e.getData(CommonDataKeys.EDITOR)
        if (editor != null) {
            handleEditorJump(editor, e.dataContext)
        } else {
            when (val component = e.getData(PlatformDataKeys.CONTEXT_COMPONENT)) {
                is JTree -> handleTreeJump(component)
                is JList<*> -> handleListJump(component)
            }
        }
    }

    private fun handleEditorJump(editor: Editor, dataContext: DataContext) {
        val handler = EditorActionManager.getInstance().getActionHandler(ideAction)

        repeat(jumpAmount) {
            handler.execute(editor, editor.caretModel.primaryCaret, dataContext)
        }
    }

    private fun handleTreeJump(tree: JTree) {
        val lead = tree.leadSelectionRow.takeIf { it >= 0 } ?: return
        val newLead = calculateNewPosition(lead, tree.rowCount)

        if (withSelection) {
            val anchor = findAnchor(lead, tree.selectionRows)
            selectRange(tree, anchor, newLead)
        } else {
            tree.setSelectionRow(newLead)
        }

        tree.scrollRowToVisible(newLead)
    }

    private fun handleListJump(list: JList<*>) {
        val lead = list.leadSelectionIndex.takeIf { it >= 0 } ?: return
        val newLead = calculateNewPosition(lead, list.model.size)

        if (withSelection) {
            val anchor = findAnchor(lead, list.selectedIndices)
            selectRange(list, anchor, newLead)
        } else {
            list.selectedIndex = newLead
        }

        list.ensureIndexIsVisible(newLead)
    }

    private fun calculateNewPosition(current: Int, maxSize: Int): Int {
        return if (isDown) {
            (current + jumpAmount).coerceAtMost(maxSize - 1)
        } else {
            (current - jumpAmount).coerceAtLeast(0)
        }
    }

    private fun findAnchor(lead: Int, selection: IntArray?): Int {
        if (selection == null || selection.isEmpty()) return lead

        val min = selection.minOrNull()!!
        val max = selection.maxOrNull()!!

        // Anchor is always the opposite end from the lead
        return if (lead == min) max else min
    }

    private fun selectRange(tree: JTree, anchor: Int, newLead: Int) {
        val min = minOf(anchor, newLead)
        val max = maxOf(anchor, newLead)

        tree.setSelectionInterval(min, max)

        // Force lead to move to newLead (trick: remove and re-add)
        tree.removeSelectionRow(newLead)
        tree.addSelectionRow(newLead)
    }

    private fun selectRange(list: JList<*>, anchor: Int, newLead: Int) {
        val min = minOf(anchor, newLead)
        val max = maxOf(anchor, newLead)

        list.setSelectionInterval(min, max)

        // Force lead to move to newLead (trick: remove and re-add)
        list.removeSelectionInterval(newLead, newLead)
        list.addSelectionInterval(newLead, newLead)
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val component = e.getData(PlatformDataKeys.CONTEXT_COMPONENT)

        e.presentation.isEnabled = editor != null ||
                                   component is JTree ||
                                   component is JList<*>
    }
}
