package com.github.nicsilver.jumpertest.action

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class AppSettingsConfigurable : Configurable {
    private var settingsComponent: AppSettingsComponent? = null

    override fun getDisplayName(): String = "Jumper Settings"

    override fun getPreferredFocusedComponent(): JComponent? =
        settingsComponent?.preferredFocusedComponent

    override fun createComponent(): JComponent? {
        settingsComponent = AppSettingsComponent()
        return settingsComponent?.panel
    }

    override fun isModified(): Boolean = true

    override fun apply() {
        // No-op: changes are applied immediately
    }

    override fun reset() {
        // No-op
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}
