package com.github.nicsilver.jumpertest.action

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.keymap.KeymapUtil
import com.intellij.openapi.options.ex.Settings
import com.intellij.openapi.ui.Messages
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import com.intellij.util.concurrency.EdtExecutorService
import com.intellij.util.ui.JBUI
import java.awt.Component
import java.awt.Dimension
import java.util.concurrent.TimeUnit
import javax.swing.*

class AppSettingsComponent {
    private val mainPanel: JPanel
    private val jumpAmountField = JBTextField(10)
    private val jumpListModel = DefaultListModel<JumpItem>()
    private val jumpList = JList(jumpListModel).apply {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
        cellRenderer = JumpListCellRenderer()
        border = JBUI.Borders.empty(4)
    }

    data class JumpItem(val amount: Int, val keybindings: Map<String, String>)

    init {
        updateJumpList()

        mainPanel = panel {
            row {
                label("Jumper Settings").apply {
                    component.font = component.font.deriveFont(16f)
                }
            }

            row {
                text(
                    "Create custom jump actions to navigate quickly through code, project files, and UI lists.",
                    maxLineLength = 80
                )
            }.topGap(TopGap.SMALL)

            group("Add Jump") {
                row("Jump Amount:") {
                    cell(jumpAmountField)
                        .comment("Number of lines/rows to jump")
                        .focused()

                    button("Add Jump") {
                        addJump()
                    }
                }
            }.topGap(TopGap.MEDIUM)

            group("Active Jumps") {
                row {
                    text("Select a jump and click 'Configure Keybindings' to set all 4 shortcuts at once")
                        .applyToComponent { font = font.deriveFont(font.size2D - 1) }
                }

                row {
                    val scrollPane = JBScrollPane(jumpList).apply {
                        preferredSize = Dimension(550, 180)
                        border = JBUI.Borders.customLine(JBColor.border(), 1)
                    }
                    cell(scrollPane)
                }.topGap(TopGap.SMALL)

                row {
                    button("Configure Keybindings") {
                        configureKeybindingsForSelected()
                    }
                    button("Remove") {
                        removeSelectedJump()
                    }
                    button("Clear All") {
                        clearAllJumps()
                    }
                }
            }.topGap(TopGap.MEDIUM)

            row {
                text(
                    "<b>Tip:</b> Each jump has 4 actions (Up, Down, Up+Select, Down+Select). " +
                    "Assign keybindings in the keymap for the ones you use most.",
                    maxLineLength = 80
                )
            }.topGap(TopGap.SMALL)
        }

        jumpList.addMouseListener(object : java.awt.event.MouseAdapter() {
            override fun mouseClicked(e: java.awt.event.MouseEvent) {
                if (e.clickCount == 2) {
                    val index = jumpList.locationToIndex(e.point)
                    if (index >= 0) {
                        val item = jumpListModel.getElementAt(index)
                        openKeymapForJump(item.amount)
                    }
                }
            }
        })
    }

    val panel: JPanel
        get() = mainPanel

    val preferredFocusedComponent: JComponent
        get() = jumpAmountField

    private fun addJump() {
        val amount = try {
            jumpAmountField.text.toInt()
        } catch (ex: NumberFormatException) {
            Messages.showErrorDialog("Please enter a valid number", "Invalid Input")
            return
        }

        if (amount <= 0) {
            Messages.showErrorDialog("Jump amount must be greater than 0", "Invalid Input")
            return
        }

        if (JumperState.getInstance().jumpCaretActions.contains(amount)) {
            Messages.showInfoMessage("Jump $amount already exists", "Already Exists")
            return
        }

        JumpManager.addJumpAction(amount)
        jumpAmountField.text = ""
        updateJumpList()

        // Select the newly added jump
        for (i in 0 until jumpListModel.size()) {
            if (jumpListModel.getElementAt(i).amount == amount) {
                jumpList.selectedIndex = i
                break
            }
        }

        val result = Messages.showYesNoDialog(
            "Jump $amount created! Would you like to configure keybindings now?",
            "Configure Keybindings",
            "Yes",
            "Later",
            Messages.getQuestionIcon()
        )

        if (result == Messages.YES) {
            // Pass amount directly, don't rely on selection
            openKeymapForJump(amount)
        }
    }

    private fun configureKeybindingsForSelected() {
        val selected = jumpList.selectedValue ?: run {
            Messages.showInfoMessage("Please select a jump to configure", "No Selection")
            return
        }

        openKeymapForJump(selected.amount)
    }

    private fun openKeymapForJump(amount: Int) {
        val settings = DataManager.getInstance()
            .getDataContext(mainPanel)
            .getData(Settings.KEY) ?: return

        val configurable = settings.find("preferences.keymap")

        // First open the keymap to let it initialize
        settings.select(configurable).doWhenDone {
            // Wait for UI to fully load, then apply search
            EdtExecutorService.getScheduledExecutorInstance().schedule({
                val searchTerm = "Jump.*$amount"
                settings.select(configurable, "\"$searchTerm\"").doWhenDone {
                    // Refresh list after keybindings might be changed
                    EdtExecutorService.getScheduledExecutorInstance().schedule({
                        updateJumpList()
                    }, 500, TimeUnit.MILLISECONDS)
                }
            }, 300, TimeUnit.MILLISECONDS)
        }
    }

    private fun removeSelectedJump() {
        val selected = jumpList.selectedValue ?: run {
            Messages.showInfoMessage("Please select a jump to remove", "No Selection")
            return
        }

        JumpManager.removeJumpAction(selected.amount)
        updateJumpList()
    }

    private fun clearAllJumps() {
        if (jumpListModel.isEmpty) return

        val result = Messages.showYesNoDialog(
            "Remove all jump actions?",
            "Clear All Jumps",
            Messages.getQuestionIcon()
        )

        if (result == Messages.YES) {
            JumperState.getInstance().jumpCaretActions.toList().forEach {
                JumpManager.removeJumpAction(it)
            }
            updateJumpList()
        }
    }

    private fun updateJumpList() {
        val selectedAmount = jumpList.selectedValue?.amount
        jumpListModel.clear()

        JumperState.getInstance().jumpCaretActions.sorted().forEach { amount ->
            val keybindings = getKeybindingsForJump(amount)
            jumpListModel.addElement(JumpItem(amount, keybindings))
        }

        // Restore selection
        if (selectedAmount != null) {
            for (i in 0 until jumpListModel.size()) {
                if (jumpListModel.getElementAt(i).amount == selectedAmount) {
                    jumpList.selectedIndex = i
                    break
                }
            }
        }
    }

    private fun getKeybindingsForJump(amount: Int): Map<String, String> {
        val actionIds = linkedMapOf(
            "↑" to "JumpUp $amount",
            "↓" to "JumpDown $amount",
            "↑+Sel" to "JumpUpSelect $amount",
            "↓+Sel" to "JumpDownSelect $amount"
        )

        return actionIds.mapValues { (_, actionId) ->
            val action = ActionManager.getInstance().getAction(actionId)
            if (action != null) {
                val shortcuts = KeymapUtil.getActiveKeymapShortcuts(actionId).shortcuts
                if (shortcuts.isNotEmpty()) {
                    shortcuts.take(2).joinToString(", ") { KeymapUtil.getShortcutText(it) }
                } else {
                    "not set"
                }
            } else {
                "not set"
            }
        }
    }

    private class JumpListCellRenderer : DefaultListCellRenderer() {
        override fun getListCellRendererComponent(
            list: JList<*>,
            value: Any?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
        ): Component {
            val label = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus) as JLabel

            if (value is JumpItem) {
                val bindingLines = value.keybindings.entries
                    .joinToString("  •  ") {
                        val status = if (it.value == "not set")
                            "<span style='color: #999;'>${it.key}: ${it.value}</span>"
                        else
                            "${it.key}: <b>${it.value}</b>"
                        status
                    }

                label.text = "<html><b>Jump ${value.amount} lines</b><br>" +
                            "<span style='font-size: 90%;'>$bindingLines</span></html>"
                label.border = JBUI.Borders.empty(8, 8)
            }

            return label
        }
    }
}
