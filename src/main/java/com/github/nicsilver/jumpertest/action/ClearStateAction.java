package com.github.nicsilver.jumpertest.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class ClearStateAction extends AnAction
{
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        JumperState.getInstance().jumpCaretActions.clear();
    }
}
