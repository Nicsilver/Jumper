package com.github.nicsilver.jumpertest.action;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.SdkIcons;
import org.jetbrains.annotations.NotNull;

public class DynamicActionGroup extends ActionGroup
{
    @NotNull
    @Override
    public AnAction @NotNull [] getChildren(AnActionEvent e)
    {
        JumperState jumperState = JumperState.getInstance();
        
        JumpCaretAction[] actions = new JumpCaretAction[jumperState.jumpCaretActions.size()];
        for (int i = 0; i < jumperState.jumpCaretActions.size(); i++)
        {
            int jump = jumperState.jumpCaretActions.get(i);
            actions[i] = new JumpCaretAction("Jump " + jump, "Jump " + jump, SdkIcons.Sdk_default_icon, jump);
        }
        
        return actions;
    }
}